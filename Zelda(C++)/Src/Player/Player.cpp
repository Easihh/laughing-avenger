#include "Player\Player.h"
#include <sstream>
#include <Windows.h>
#include <iostream>
#include "Misc\Tile.h"
#include "Item\Bomb.h"
#include "Item\Boomrang.h"
#include "Item\Arrow.h"
#include "Item\Candle.h"
#include "Item\Potion.h"
#include "Utility\Point.h"
#include"Utility\PlayerInfo.h"
#include "Misc\Marker\ShopMarker.h"
#include "Misc\WorldMap.h"
#include "Misc\NPC.h"
#include "Misc\MoveableBlock.h"
 Player::Player(Point pos){
	 depth = 100;
	 stepToMove = 0;
	 currentLayer=prevLayer =OverWorld;
	 stepToAlign = currentInvincibleFrame = transitionStep = xOffset = yOffset = 0;
	 movePlayerToNewVector = stepIsNegative = movingSwordIsActive = isObtainingItem = false;
	 isAttacking = isScreenTransitioning = isInvincible = boomerangIsActive = arrowIsActive= false;
	 inputIsDisabled = false;
	 canAttack = inventoryKeyReleased = itemKeyReleased = attackKeyReleased=hasMovedFromEntranceDoor = true;
	 boss1IsAlive = true;
	 position = pos;
	 worldX = (int)(position.y / Global::roomHeight);
	 worldY = (int)(position.x / Global::roomWidth);
	 prevWorldX = worldX;
	 prevWorldY = worldY;
	 width = Global::TileWidth;
	 height = Global::TileHeight;
	 dir = Direction::Up;
	 loadImage();
	 setupMask(&fullMask, width, height, sf::Color::Magenta);
	 inventory = std::make_unique<Inventory>(worldX,worldY);
	 Point pt(0, 0);
	 inventory->items.push_back(std::make_unique<Boomrang>(pt, "MagicalBoomerang"));
	 inventory->items.push_back(std::make_unique<Bomb>(pt, "Bomb"));
	 inventory->items.push_back(std::make_unique<Arrow>(pt, "Arrow"));
	 inventory->items.push_back(std::make_unique<Candle>(pt, "Candle"));
	 inventory->items.push_back(std::make_unique<Candle>(pt, "Food"));
	 inventory->items.push_back(std::make_unique<Potion>(pt, "RedPotion"));
	 inventory->items.push_back(std::make_unique<Candle>(pt, "Whistle"));
	 inventory->items.push_back(std::make_unique<Candle>(pt, "MagicalRod"));
}
 void Player::loadImage(){
	walkingAnimation.push_back(std::make_unique<Animation>("Link_Movement", width, height, position, 6));
	walkingAnimation.push_back(std::make_unique<Animation>("Link_Movement_Hit1", width, height, position, 6));
	walkingAnimation.push_back(std::make_unique<Animation>("Link_Movement_Hit2", width, height, position, 6));
	walkAnimationIndex = 0;
	attackAnimation.push_back(std::make_unique<Animation>("Link_Attack", width, height, position, NULL));
	attackAnimation.push_back(std::make_unique<Animation>("Link_Attack_Hit1", width, height, position, NULL));
	attackAnimation.push_back(std::make_unique<Animation>("Link_Attack_Hit2", width, height, position, NULL));
	attackAnimationIndex = 0;
	texture.loadFromFile("Tileset/Link_ObtainItem.png");
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
 }
 void Player::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	 checkIfMovedFromEntrance(worldMap);
	 if (isNearBoss())
		 Sound::playSound(GameSound::BossScream1);
	 else Sound::stopSound(GameSound::BossScream1);
	 if(sword!=NULL)
		sword->update(isAttacking, canAttack, worldMap, &walkingAnimation);
	 if (pushbackStep!=0)
		playerPushbackUpdate();
	 checkInventoryInput();
	 if (!inputIsDisabled){
		 checkAttackInput();
		 checkItemUseInput(worldMap);
		 if (pushbackStep == 0)
			 checkMovementInput(worldMap);
	 }
	 if (stepToMove != 0)
		 completeMove();
	 if (stepToAlign != 0)
		 snapToGrid();
	 attackAnimation[attackAnimationIndex]->updateAnimationFrame(dir, position);
	 if(isScreenTransitioning)
		 screenTransition();
	 checkInvincible();
	 fullMask->setPosition(position.x, position.y);
	 inventory->playerBar->update();
 }
 bool Player::isNearBoss(){
	 bool isNearBoss = false;
	 if (inventory->playerBar->currentDungeon != DungeonLevel::NONE && boss1IsAlive){
		 switch (inventory->playerBar->currentDungeon){
		 case DungeonLevel::ONE:
			 if (worldX == 0 && worldY == 3 ||
				 worldX == 1 && worldY == 3)
				 isNearBoss = true;
			 break;
		 }
	 }
	 return isNearBoss;
 }
 void Player::movePlayerToDungeonEntrance(){
	 movePlayerToNewVector = true;
	 inputIsDisabled = false;
	 worldX = 3;
	 worldY = 3;
	 float teleportX = worldY*Global::roomWidth + (0.5*Global::roomWidth);
	 float teleportY = worldX*Global::roomHeight + Global::roomHeight + Global::inventoryHeight - 2 * Global::TileHeight;
	 float inventoryNewX = worldY*Global::roomHeight;
	 float inventoryNewY = worldX*Global::roomWidth;
	 position = Point(teleportX, teleportY);
	 inventory->setInventoryPosition(Point(inventoryNewX, inventoryNewY));
	 inventory->playerBar->setPlayerBar(Point(inventoryNewX, inventoryNewY));
	 inventory->playerBar->resetDungeonPlayerMarker();
	 Global::gameView.setCenter(worldY*Global::roomWidth + Global::SCREEN_WIDTH / 2,
		 worldX*Global::roomHeight + Global::SCREEN_HEIGHT / 2);
	 for (int i = 0; i < 3; i++)
		 walkingAnimation[i]->updateAnimationFrame(dir, position);
	 fullMask->setPosition(position.x, position.y);
	 Sound::playSound(GameSound::Underworld);
 }
 void Player::checkIfMovedFromEntrance(std::vector<std::shared_ptr<GameObject>>* worldMap){
	 //there wont be more than 1 entrance per room.
	 for (auto& obj : *worldMap){
		 if (dynamic_cast<ShopMarker*>(obj.get())){
			 ShopMarker* marker = (ShopMarker*)obj.get();
			 int yDistance = marker->position.y - position.y;
			 if (std::abs(yDistance) >= Global::TileHeight)
				 hasMovedFromEntranceDoor = true;
		 }
			 
	 }
 }
 void Player::playerPushBack(std::vector<std::shared_ptr<GameObject>>* worldMap,Point monsterPosition) {
	 float pushBackMinDistance = 0;
	 switch(dir){
	 case Direction::Up:
		 if(monsterPosition.y < position.y){
			 pushBackMinDistance = getMinimumLineCollisionDistance(Direction::Down, worldMap);
			 if(!isOutsideRoomBound(Point(position.x, position.y + pushBackMinDistance)))
				 pushbackStep = -pushBackMinDistance;
			 // the maximum pushback is beyond the room bounds
			 else pushbackStep = -getDistanceToMapBoundary(Direction::Down);
		 }
		 else if(monsterPosition.y > position.y){
			 pushBackMinDistance = getMinimumLineCollisionDistance(Direction::Up, worldMap);
			 if(!isOutsideRoomBound(Point(position.x, position.y - pushBackMinDistance)))
				 pushbackStep = pushBackMinDistance;
			 else pushbackStep = getDistanceToMapBoundary(Direction::Up);
		 }
		 break;
	 case Direction::Down:
		 if(monsterPosition.y < position.y){
			 pushBackMinDistance = getMinimumLineCollisionDistance(Direction::Down, worldMap);
			 if(!isOutsideRoomBound(Point(position.x, position.y + pushBackMinDistance)))
				 pushbackStep = pushBackMinDistance;
			 // the maximum pushback is beyond the room bounds
			 else pushbackStep = getDistanceToMapBoundary(Direction::Down);
		 }
		 else if(monsterPosition.y > position.y){
			 pushBackMinDistance = getMinimumLineCollisionDistance(Direction::Up, worldMap);
			 if(!isOutsideRoomBound(Point(position.x, position.y - pushBackMinDistance)))
				 pushbackStep = -pushBackMinDistance;
			 else pushbackStep = -getDistanceToMapBoundary(Direction::Up);
		 }
		 break;
	 case Direction::Right:
		if(monsterPosition.x < position.x){
			 pushBackMinDistance = getMinimumLineCollisionDistance(Direction::Right, worldMap);
			 if(!isOutsideRoomBound(Point(position.x + pushBackMinDistance, position.y)))
				 pushbackStep = pushBackMinDistance;
			 else pushbackStep = getDistanceToMapBoundary(Direction::Right);
		 }
		else if(monsterPosition.x > position.x){
			pushBackMinDistance = getMinimumLineCollisionDistance(Direction::Left, worldMap);
			if(!isOutsideRoomBound(Point(position.x - pushBackMinDistance, position.y)))
				pushbackStep = -pushBackMinDistance;
			else pushbackStep = -getDistanceToMapBoundary(Direction::Left);
		}
		 break;
	 case Direction::Left:
		 if(monsterPosition.x < position.x){
			 pushBackMinDistance = getMinimumLineCollisionDistance(Direction::Right, worldMap);
			 if(!isOutsideRoomBound(Point(position.x + pushBackMinDistance, position.y)))
				 pushbackStep = -pushBackMinDistance;
			 else pushbackStep = -getDistanceToMapBoundary(Direction::Right);
		 }
		 else if(monsterPosition.x > position.x){
			 pushBackMinDistance = getMinimumLineCollisionDistance(Direction::Left, worldMap);
			 if(!isOutsideRoomBound(Point(position.x - pushBackMinDistance, position.y)))
				 pushbackStep = pushBackMinDistance;
			 else pushbackStep = getDistanceToMapBoundary(Direction::Left);
		 }
		 break;
	 }
 }
 void Player::playerPushbackUpdate() {
	 //positive step mean moving in same direction as current direction.
	 int step = std::abs(pushbackStep);
	 switch(dir){
	 case Direction::Down:
		 if(pushbackStep<0)
			 position.y -= step;
		 else position.y += step;
		 break;
	 case Direction::Up:
		 if(pushbackStep<0)
			position.y += step;
		 else position.y -= step;
		 break;
	 case Direction::Left:
		if(pushbackStep<0)
		 position.x += step;
		else position.x -= step;
		break;
	 case Direction::Right:
		 if(pushbackStep<0)
			 position.x -= step;
		 else position.x += step;
		 break;
	 }
	 pushbackStep = 0;
 }
 void Player::checkInventoryInput(){
	 if (sf::Keyboard::isKeyPressed(sf::Keyboard::Q) && inventoryKeyReleased){
		 Static::gameState = GameState::InventoryMenu;
		 inventoryKeyReleased = false;
		 inventory->transitionToInventory();
	 }
 }
 void Player::checkAttackInput(){
	 if (sf::Keyboard::isKeyPressed(sf::Keyboard::Space)){
		 if (canAttack && !isAttacking && attackKeyReleased && inventory->playerBar->mySword!=SwordType::None && !isObtainingItem){
			 sword =std::make_unique<Sword>(position, dir);
			 if(!inventory->playerBar->isFullHP())
				 Sound::playSound(GameSound::SoundType::SwordAttack);
			 if(!movingSwordIsActive && inventory->playerBar->isFullHP()){
				 Sound::playSound(GameSound::SoundType::SwordCombineAttack);
				 movingSword = std::make_shared<MovingSword>(position, dir,sword->strength);
				 Static::toAdd.push_back(movingSword);
				 movingSwordIsActive = true;
			 }
			 isAttacking = true;
			 canAttack = false;
			 attackKeyReleased = false;
		 }
	 }
 }
 void Player::checkMovementInput(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	 bool movementKeyPressed = false;
	 bool outsideBound = false;
	 if (sf::Keyboard::isKeyPressed(sf::Keyboard::Left) && !isScreenTransitioning && !isObtainingItem){
		 movementKeyPressed = true;
		 if (stepToMove == 0 && !isAttacking){
			 if (dir != Direction::Left){
				 getUnalignedCount(Direction::Left);
				 dir = Direction::Left;
				 walkingAnimation[walkAnimationIndex]->reset();
			 }
			 if (!isColliding(worldMap,fullMask,getXOffset(),getYOffset()) && stepToAlign == 0)
				 stepToMove = Global::minStep;
			 outsideBound = isOutsideRoomBound(Point(position.x - stepToMove, position.y));
		 }
	 }
	 else if(sf::Keyboard::isKeyPressed(sf::Keyboard::Right) && !isScreenTransitioning && !isObtainingItem){
		 movementKeyPressed = true;
		 if (stepToMove == 0 && !isAttacking){
			 if (dir != Direction::Right){
				 getUnalignedCount(Direction::Right);
				 dir = Direction::Right;
				 walkingAnimation[walkAnimationIndex]->reset();
			 }
			 if (!isColliding(worldMap, fullMask, getXOffset(), getYOffset()) && stepToAlign == 0)
				 stepToMove = Global::minStep;
			 outsideBound = isOutsideRoomBound(Point(position.x+stepToMove, position.y));
		 }
	 }
	 else if(sf::Keyboard::isKeyPressed(sf::Keyboard::Up) && !isScreenTransitioning && !isObtainingItem){
		 movementKeyPressed = true;
		 if (stepToMove == 0 && !isAttacking){
			 if (dir != Direction::Up){
				 getUnalignedCount(Direction::Up);
				 dir = Direction::Up;
				 walkingAnimation[walkAnimationIndex]->reset();
			 }
			 if (!isColliding(worldMap, fullMask, getXOffset(), getYOffset()) && stepToAlign == 0)
				 stepToMove = Global::minStep;
			 outsideBound = isOutsideRoomBound(Point(position.x, position.y - stepToMove));
		 }
	 }
	 else if(sf::Keyboard::isKeyPressed(sf::Keyboard::Down) && !isScreenTransitioning && !isObtainingItem){
		 movementKeyPressed = true;
		 if (stepToMove == 0 && !isAttacking){
			 if (dir != Direction::Down){
				 getUnalignedCount(Direction::Down);
				 dir = Direction::Down;
				 walkingAnimation[walkAnimationIndex]->reset();
			 }
			 if (!isColliding(worldMap, fullMask, getXOffset(), getYOffset()) && stepToAlign == 0)
				 stepToMove = Global::minStep;
			 outsideBound = isOutsideRoomBound(Point(position.x, position.y+stepToMove));
		 }
	 }
	 if (movementKeyPressed){
		 for(int i = 0; i < 3; i++)
			 walkingAnimation[i]->updateAnimationFrame(dir, position);
	 }
	 if (outsideBound){
		 prevLayer = currentLayer;
		 transitionStep = maxTransitionStep;
		 isScreenTransitioning = true;
	 }
 }
 void Player::checkItemUseInput(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	 if (sf::Keyboard::isKeyPressed(sf::Keyboard::S) && itemKeyReleased){
		 inventory->itemUse(position, dir, worldMap);
		 itemKeyReleased = false;
	 }
 }
 void  Player::takeDamage(std::vector<std::shared_ptr<GameObject>>* worldMap,Monster* monster) {
	 if (!isInvincible && !isScreenTransitioning){
		 if(monster->strength>0){
			 Sound::playSound(GameSound::SoundType::TakeDamage);
			 inventory->playerBar->decreaseCurrentHP(monster->strength);
			 playerPushBack(worldMap, monster->position);
			 walkAnimationIndex = 1;
			 attackAnimationIndex = 1;
			 if(isAttacking)
				 sword->endSword();
			 walkingAnimation[walkAnimationIndex]->sprite.setPosition(position.x, position.y);
			 attackAnimation[attackAnimationIndex]->sprite.setPosition(position.x, position.y);
			 if(inventory->playerBar->getCurrentHP() <= 0)
				 std::cout << "I'm Dead";
			 else isInvincible = true;
		 }
	 }
 }
 bool Player::isColliding(std::vector<std::shared_ptr<GameObject>>* worldMap, std::unique_ptr<sf::RectangleShape>& mask, float xOffset, float yOffset) {
	 bool collision = false;
	 Point offset(xOffset, yOffset);
	 for(auto& obj : *worldMap)
	 {
		 //Tile mask/setup is not done at object creation but at the first update for loading time purpose
		 if (dynamic_cast<Tile*>(obj.get())){
			 Tile* temp = (Tile*)obj.get();
			 if (temp->hasBeenSetup && intersect(mask, obj->fullMask, offset) && obj->isCollideable){
				 collision = true;
			if (temp->id == (int)TileType::DungeonTile89 || temp->id == (int)TileType::DungeonTile90)
				checkDungeonKeyDoor(worldMap,temp);
			 }
		 }
		 if (dynamic_cast<NPC*>(obj.get())|| dynamic_cast<MoveableBlock*>(obj.get()))
			if (intersect(mask, obj->fullMask, offset) && obj->isCollideable)
				collision = true;
	 }
	 return collision;
 }
 void Player::checkDungeonKeyDoor(std::vector<std::shared_ptr<GameObject>>* Worldmap, GameObject* closedKeyDoor){
	 Tile* temp = (Tile*)closedKeyDoor;
	 if (inventory->playerBar->keysAmount >= 1){
		 switch (temp->id){
		 case (int)TileType::DungeonTile89:
			 temp->texture.loadFromFile("Tileset/Dungeon/dungeonTile24.png");
			 temp->sprite.setTexture(temp->texture);
			 temp->isCollideable = false;
			 temp->id = (int)TileType::DungeonTile24;
			 openKeyedDoor(Worldmap, (int)TileType::DungeonTile90);//find and change the 2nd part of the door to open
			 break;
		 case (int)TileType::DungeonTile90:
			 temp->texture.loadFromFile("Tileset/Dungeon/dungeonTile25.png");
			 temp->sprite.setTexture(temp->texture);
			 temp->isCollideable = false;
			 openKeyedDoor(Worldmap, (int)TileType::DungeonTile89);
			 break;
		 }
		 inventory->playerBar->keysAmount -= 1;
		 Sound::playSound(GameSound::DoorUnlock);
	 }
 }
 void Player::openKeyedDoor(std::vector<std::shared_ptr<GameObject>>* Worldmap,int id){
	 for (auto& obj : *Worldmap)
	 {
		 if (dynamic_cast<Tile*>(obj.get())){
			 Tile* temp = (Tile*)obj.get();
			 if (temp->id == id){
				 switch (temp->id){
				 case (int)TileType::DungeonTile90:
					 temp->texture.loadFromFile("Tileset/Dungeon/dungeonTile25.png");
					 temp->id = (int)TileType::DungeonTile25;
					 break;
				 case (int)TileType::DungeonTile89:
					 temp->texture.loadFromFile("Tileset/Dungeon/dungeonTile24.png");
					 temp->id = (int)TileType::DungeonTile24;
					 break;
				 }
				 temp->sprite.setTexture(temp->texture);
				 temp->isCollideable = false;
			 }
		 }
	 }
 }
 void Player::checkInvincible(){
	 if (isInvincible){
		 currentInvincibleFrame++;
		 if (currentInvincibleFrame % 6 == 0){//switch image back and forth every 6 frames;
			 if (walkAnimationIndex == 1){
				 walkAnimationIndex++;
				 attackAnimationIndex++;
			 }
			 else 
			 {
				 walkAnimationIndex--;
				 attackAnimationIndex--;
			 }
			 for(int i = 0; i < 3; i++){
				 walkingAnimation[i]->updateAnimationFrame(dir, position);
				 attackAnimation[i]->updateAnimationFrame(dir, position);
			 }
		 }
		 if (currentInvincibleFrame >= maxInvincibleFrame){
			 isInvincible = false;
			 currentInvincibleFrame = 0;
			 walkAnimationIndex = 0;
			 attackAnimationIndex = 0;
			 for(int i = 0; i < 3; i++){
				 walkingAnimation[i]->updateAnimationFrame(dir, position);
				 attackAnimation[i]->updateAnimationFrame(dir, position);
			 }
		 }
	 }
 }
 void Player::endScreenTransition(){
	 stepToMove = Global::TileHeight + 1;
	 isScreenTransitioning = false;
	 movePlayerToNewVector = true;
	 prevWorldX = worldX;
	 prevWorldY = worldY;
	 switch (dir){
	 case Direction::Down:
		worldX++;
		break;
	 case Direction::Up:
		 worldX--;
		 break;
	 case Direction::Right:
		 worldY++;
		 break;
	 case Direction::Left:
		 worldY--;
		 break;
	 }
	 inventory->playerBar->updatePlayerMapMarker(dir);
 }
 void Player::screenTransition(){
	 int minTransitionStep = 2;
	 float increaseStep = (float)(maxTransitionStep / minTransitionStep);
	 float viewX = Global::gameView.getCenter().x;
	 float viewY = Global::gameView.getCenter().y;
	 float nextXPosition=0;
	 float nextYPosition = 0;
	 switch (dir){

	 case Direction::Right:
		 Global::gameView.setCenter(viewX + (float)((Global::roomWidth / (increaseStep))),viewY);
		 nextXPosition = (float)minTransitionStep*Global::roomWidth / maxTransitionStep;
		 break;

	 case Direction::Left:
		 Global::gameView.setCenter(viewX - (float)(Global::roomWidth / (increaseStep)),viewY);
		 nextXPosition = -((float)minTransitionStep*Global::roomWidth / maxTransitionStep);
		 break;

	 case Direction::Down:
		 Global::gameView.setCenter(viewX, viewY + (Global::roomHeight / (increaseStep)));
		 nextYPosition = (float)minTransitionStep*Global::roomHeight / maxTransitionStep;
		 break;
	 case Direction::Up:
		 Global::gameView.setCenter(viewX, viewY - (Global::roomHeight / (increaseStep)));
		 nextYPosition = -((float)minTransitionStep*Global::roomHeight / maxTransitionStep);
		 break;
	 }
	 Point nextPosition(nextXPosition, nextYPosition);
	 inventory->playerBar->setBarNextPosition(nextPosition);
	 inventory->updateInventoryPosition(nextPosition);
	 walkingAnimation[walkAnimationIndex]->updateAnimationFrame(dir, position);
	 transitionStep -= minTransitionStep;
	 if (transitionStep == 0){
		 endScreenTransition();
	 }
 }
 int Player::getXOffset(){
	 xOffset = 0;
	if (dir == Direction::Left)
		 xOffset = -Global::minStep;
	 else if (dir == Direction::Right)
		 xOffset = Global::minStep;
	 return xOffset;
 }
 int Player::getYOffset(){
	 yOffset = 0;
	 if (dir == Direction::Up)
		 yOffset = -Global::minStep;
	 else if (dir == Direction::Down)
		 yOffset = Global::minStep;
	 return yOffset;
 }
 void Player::getUnalignedCount(Direction nextDir){
	 int x = (int)position.x;
	 int y = (int)position.y;
	 stepIsNegative = false;
	 switch (nextDir){
	 case Direction::Right:
		 stepToAlign = Global::HalfTileHeight -( y %Global::HalfTileHeight);
		 break;
	 case Direction::Left:
		 stepToAlign = Global::HalfTileHeight - (y %Global::HalfTileHeight);
		 break;
	 case Direction::Up:
		 stepToAlign = Global::HalfTileWidth - (x %Global::HalfTileWidth);
		 break;
	 case Direction::Down:
		 stepToAlign = Global::HalfTileWidth - (x %Global::HalfTileWidth);
		 break;
	 }
	 if (stepToAlign >= Global::minGridStep/2){
		 stepToAlign = Global::minGridStep - stepToAlign;
		 stepIsNegative = true;
	 }
	 if (stepToAlign == Global::minGridStep)stepToAlign = 0;
 }
 void Player::snapToGrid(){
	 switch (dir){
	 case Direction::Right:
	 case Direction::Left:
		 if (!stepIsNegative){
			 if (stepToAlign<Global::minStep)
				 position.y += stepToAlign;
			 else  position.y += Global::minStep;
		 }
		 else 
		 {
			 if (stepToAlign<Global::minStep)
				 position.y -= stepToAlign;
			 else  position.y -= Global::minStep;
		 }
		 break;
	 case Direction::Up:
	 case Direction::Down:
		 if (!stepIsNegative){
			 if (stepToAlign < Global::minStep)
				 position.x += stepToAlign;
			 else position.x += Global::minStep;
		 }
		 else
		 {
			 if (stepToAlign < Global::minStep)
				 position.x -= stepToAlign;
			 else position.x -= Global::minStep;
		 }
		 break;
	 }
	 if (stepToAlign < Global::minStep)
		 stepToAlign = 0;
	 else stepToAlign -= Global::minStep;
	 updateSprites();
 }
 void Player::completeMove(){
	 stepToMove -= Global::minStep;
	 switch (dir){
	 case Direction::Right:
		 position.x += Global::minStep;
			 break;
	 case Direction::Left:
		 position.x -= Global::minStep;
			 break;
	 case Direction::Up:
		 position.y -= Global::minStep;
			 break;
	 case Direction::Down:
		 position.y += Global::minStep;
			 break;
	 }
	 updateSprites();
 }
 void Player::updateSprites(){
	 walkingAnimation[walkAnimationIndex]->sprite.setPosition(position.x, position.y);
	 attackAnimation[attackAnimationIndex]->sprite.setPosition(position.x, position.y);
 }
 void Player::draw(sf::RenderWindow& mainWindow){
	 mainWindow.setView(Global::gameView);
	 inventory->playerBar->draw(mainWindow);
	 if (!isAttacking && !isObtainingItem)
		 mainWindow.draw(walkingAnimation[walkAnimationIndex]->sprite);
	 else if(isAttacking && !isObtainingItem){
		 mainWindow.draw(attackAnimation[attackAnimationIndex]->sprite);
		 mainWindow.draw(sword->sprite);
		 //mainWindow.draw(*(sword->fullMask));
	 }
	 if(isObtainingItem)
		 mainWindow.draw(sprite);
	 drawText(mainWindow);
	// mainWindow.draw(*fullMask);
 }
 void Player::drawText(sf::RenderWindow& mainWindow){
	/* sf::Font font;
	 std::stringstream pos;
	 pos << "X:" << position.x << std::endl << "Y:" << position.y << std::endl
		 <<"WorldX:"<<worldX <<std::endl <<"WorldY:"<<worldY<<std::endl;
	 font.loadFromFile("arial.ttf");
	 sf::Text txt(pos.str(), font);
	 txt.setColor(sf::Color::Red);
	 txt.setPosition(position.x, position.y - 64);
	 txt.setCharacterSize(textSize);
	 mainWindow.draw(txt);*/
 }