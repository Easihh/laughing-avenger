#include "Player\Player.h"
#include <sstream>
#include <Windows.h>
#include <iostream>
#include "Misc\Tile.h"
#include "Item\Bomb.h"
#include "Utility\Point.h"
#include"Utility\PlayerInfo.h"
 Player::Player(Point pos){
	 position = pos;
	 worldX = (int)(position.y / Global::roomHeight);
	 worldY = (int)(position.x / Global::roomWidth);
	 width = Global::TileWidth;
	 height = Global::TileHeight;
	 dir = Static::Direction::Up;
	 canAttack = true;
	 isAttacking = false;
	 isScreenTransitioning = false;
	 isInvincible = false;
	 loadImage();
	 setupFullMask();
	 playerBar = new PlayerBar();
	 inventory = new Inventory();
	 Point pt(0, 0);
	 inventory->items[0][0] = new Item(pt, "MagicalBoomerang");
	 inventory->items[2][2] = new Bomb(pt, "Bomb");
	 inventoryKeyReleased = true;
	 itemKeyReleased = true;
	 attackKeyReleased = true;
}
 Player::~Player(){}
 void Player::loadImage(){
	 walkingAnimation[0] = new Animation("Link_Movement", width, height, position, 6);
	 walkingAnimation[1] = new Animation("Link_Movement_Hit1", width, height, position, 6);
	 walkingAnimation[2] = new Animation("Link_Movement_Hit2", width, height, position, 6);
	walkAnimationIndex = 0;
	attackAnimation[0] = new Animation("Link_Attack", width, height, position, NULL);
	attackAnimation[1] = new Animation("Link_Attack_Hit1", width, height, position, NULL);
	attackAnimation[2] = new Animation("Link_Attack_Hit2", width, height, position, NULL);
	attackAnimationIndex = 0;
 }
 void Player::update(std::vector<GameObject*>* worldMap){
	 sword->update(isAttacking, canAttack, worldMap, walkingAnimation);
	 checkMovementInput(worldMap);
	 checkAttackInput();
	 checkInventoryInput();
	 checkItemUseInput(worldMap);
	 if (stepToMove != 0)
		 completeMove();
	 if (stepToAlign != 0)
		 snapToGrid();
	 attackAnimation[attackAnimationIndex]->updateAnimationFrame(dir, position);
	 if(isScreenTransitioning)
		 screenTransition();
	 if (isCollidingWithMonster(worldMap))
		 takeDamage(worldMap);
	 checkInvincible();
	 fullMask->setPosition(position.x, position.y);
	 playerBar->update();
 }
 void Player::checkInventoryInput(){
	 if (sf::Keyboard::isKeyPressed(sf::Keyboard::Q) && inventoryKeyReleased){
		 Static::gameState = Static::GameState::Inventory;
		 inventoryKeyReleased = false;
		 inventory->transitionToInventory(playerBar);
	 }
 }
 void Player::checkAttackInput(){
	 if (sf::Keyboard::isKeyPressed(sf::Keyboard::Space)){
		 if (canAttack && !isAttacking && attackKeyReleased){
			 delete sword;
			 sword = new Sword(position, dir);
			 isAttacking = true;
			 canAttack = false;
			 attackKeyReleased = false;
		 }
	 }
 }
 void Player::checkMovementInput(const std::vector<GameObject*>* worldMap){
	 bool movementKeyPressed = false;
	 bool outsideBound = false;
	 if (sf::Keyboard::isKeyPressed(sf::Keyboard::Left) && !isScreenTransitioning){
		 movementKeyPressed = true;
		 if (stepToMove == 0 && !isAttacking){
			 if (dir != Static::Direction::Left){
				 getUnalignedCount(Static::Direction::Left);
				 dir = Static::Direction::Left;
				 walkingAnimation[walkAnimationIndex]->reset();
			 }
			 if (!isColliding(worldMap,fullMask,getXOffset(),getYOffset()) && stepToAlign == 0)
				 stepToMove = Global::minStep;
			 outsideBound = isOutsideMapBound(Point(position.x - stepToMove, position.y));
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Right) && !isScreenTransitioning){
		 movementKeyPressed = true;
		 if (stepToMove == 0 && !isAttacking){
			 if (dir != Static::Direction::Right){
				 getUnalignedCount(Static::Direction::Right);
				 dir = Static::Direction::Right;
				 walkingAnimation[walkAnimationIndex]->reset();
			 }
			 if (!isColliding(worldMap, fullMask, getXOffset(), getYOffset()) && stepToAlign == 0)
				 stepToMove = Global::minStep;
			 outsideBound = isOutsideMapBound(Point(position.x + width, position.y));
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Up) && !isScreenTransitioning){
		 movementKeyPressed = true;
		 if (stepToMove == 0 && !isAttacking){
			 if (dir != Static::Direction::Up){
				 getUnalignedCount(Static::Direction::Up);
				 dir = Static::Direction::Up;
				 walkingAnimation[walkAnimationIndex]->reset();
			 }
			 if (!isColliding(worldMap, fullMask, getXOffset(), getYOffset()) && stepToAlign == 0)
				 stepToMove = Global::minStep;
			 outsideBound = isOutsideMapBound(Point(position.x, position.y - stepToMove));
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Down) && !isScreenTransitioning){
		 movementKeyPressed = true;
		 if (stepToMove == 0 && !isAttacking){
			 if (dir != Static::Direction::Down){
				 getUnalignedCount(Static::Direction::Down);
				 dir = Static::Direction::Down;
				 walkingAnimation[walkAnimationIndex]->reset();
			 }
			 if (!isColliding(worldMap, fullMask, getXOffset(), getYOffset()) && stepToAlign == 0)
				 stepToMove = Global::minStep;
			 outsideBound = isOutsideMapBound(Point(position.x, position.y + height));
		 }
	 }
	 if (movementKeyPressed){
		 walkingAnimation[walkAnimationIndex]->updateAnimationFrame(dir, position);
	 }
	 if (outsideBound){
		 transitionStep = maxTransitionStep;
		 isScreenTransitioning = true;
	 }
 }
 void Player::checkItemUseInput(std::vector<GameObject*>* worldMap){
	 int INVALID_INVENTORY_INDEX = -1;
	 if (sf::Keyboard::isKeyPressed(sf::Keyboard::S) && itemKeyReleased){
		 int i = inventory->selectorInventoryXIndex;
		 int j = inventory->selectorInventoryYIndex;
		 if (i != INVALID_INVENTORY_INDEX && j != INVALID_INVENTORY_INDEX){
			 PlayerInfo info(position, playerBar->bombPtr, playerBar->diamondPtr, playerBar->keysPtr, dir);
			 inventory->items[i][j]->onUse(info, worldMap);
			 itemKeyReleased = false;
		 }
	 }
 }
 bool Player::isCollidingWithMonster(const std::vector<GameObject*>* worldMap){
	 bool isColliding = false;
	 Point offset(xOffset, yOffset);
	 for each (GameObject* obj in *worldMap)
	 {
			 if (dynamic_cast<Monster*>(obj))
				 if (intersect(fullMask, ((Monster*)obj)->mask, offset)){
					 isColliding = true;
					 collidingMonster = (Monster*)obj;
					 break;
				}
	 }
	 return isColliding;
 }
 void  Player::takeDamage(const std::vector<GameObject*>* worldMap){
	 if (!isInvincible && !isScreenTransitioning){
		 playerBar->decreaseCurrentHP(collidingMonster->strength);
		 walkAnimationIndex = 1;
		 attackAnimationIndex = 1;
		 pushback(worldMap);
		 if (playerBar->getCurrentHP() <= 0)
			 std::cout << "I'm Dead";
		 else isInvincible = true;
	 }
 }
 bool Player::isColliding(const std::vector<GameObject*>* worldMap, sf::RectangleShape* mask, float xOffset, float yOffset){
	 bool collision = false;
	 Point offset(xOffset, yOffset);
	 for each (GameObject* obj in *worldMap)
	 {
			 if (dynamic_cast<Tile*>(obj))
				 if (intersect(mask, obj->fullMask, offset)){
					 collision = true;
					 std::cout << "CollisionX:" << obj->position.x << std::endl;
					 std::cout << "CollisionY:" << obj->position.y << std::endl;
				 }
	 }
	 return collision;
 }
 void Player::pushback(const std::vector<GameObject*>* worldMap){
	 float intersectWidth;
	 float intersectHeight=2 * Global::TileHeight;
	 float pushBackDistance = 64;
	 sf::RectangleShape* pushbackLineCheck = new sf::RectangleShape();

	 sf::Vector2f size(width, height);
	 pushbackLineCheck->setSize(size);
	 pushbackLineCheck->setPosition(position.x, position.y);

	 switch (dir){
	 case Static::Direction::Down:
		 if (!isColliding(worldMap, pushbackLineCheck, 0, -intersectHeight))
			 if (!isOutsideMapBound(Point(position.x, position.y - pushBackDistance)))
				 position.y -= pushBackDistance;
		 break;
	 case Static::Direction::Up:
		 if (!isColliding(worldMap, pushbackLineCheck, 0, intersectHeight))
			 if (!isOutsideMapBound(Point(position.x, position.y + pushBackDistance)))
				 position.y += pushBackDistance;
		 break;
	 case Static::Direction::Left:
		 intersectWidth = 3 * Global::TileWidth;
		 if (!isColliding(worldMap, pushbackLineCheck, intersectWidth, 0))
			 if (!isOutsideMapBound(Point(position.x + pushBackDistance, position.y)))
				 position.x += pushBackDistance;
		 break;
	 case Static::Direction::Right:
		 intersectWidth = 2 * Global::TileWidth;
		 if (!isColliding(worldMap, pushbackLineCheck, - intersectWidth, 0))
			 if (!isOutsideMapBound(Point(position.x - pushBackDistance, position.y)))
				 position.x -= pushBackDistance;
		 break;
	 }
	 if (isAttacking)
		sword->endSword();
	 walkingAnimation[walkAnimationIndex]->sprite.setPosition(position.x, position.y);
	 attackAnimation[attackAnimationIndex]->sprite.setPosition(position.x, position.y);
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
			 walkingAnimation[walkAnimationIndex]->updateAnimationFrame(dir, position);
			 attackAnimation[attackAnimationIndex]->updateAnimationFrame(dir, position);
		 }
		 if (currentInvincibleFrame >= maxInvincibleFrame){
			 isInvincible = false;
			 currentInvincibleFrame = 0;
			 walkAnimationIndex = 0;
			 attackAnimationIndex = 0;
			 attackAnimation[attackAnimationIndex]->updateAnimationFrame(dir, position);
			 walkingAnimation[walkAnimationIndex]->updateAnimationFrame(dir, position);
		 }
	 }
 }
 bool Player::isOutsideMapBound(Point pos){
	 bool outsideBoundary = false;
	 if (pos.x > (Global::roomWidth*worldY) + Global::roomWidth)
		 outsideBoundary = true;
	 else if (pos.x < (Global::roomWidth*worldY))
		 outsideBoundary = true;
	 else if (pos.y < (Global::roomHeight*worldX) + Global::inventoryHeight)
		 outsideBoundary = true;
	 else if (pos.y >(Global::roomHeight*worldX) + Global::roomHeight + Global::inventoryHeight)
		 outsideBoundary = true;
	 return outsideBoundary;
 }
 void Player::endScreenTransition(){
	 stepToMove = Global::TileHeight + 1;
	 isScreenTransitioning = false;
	 float markerX = playerBar->playerMarker.getPosition().x;
	 float markerY = playerBar->playerMarker.getPosition().y;

	 switch (dir){
	 case Static::Direction::Down:
		worldX++;
		playerBar->markerY += Global::playerMarkerHeight;
		break;
	 case Static::Direction::Up:
		 worldX--;
		 playerBar->markerY -= Global::playerMarkerHeight;
		 break;
	 case Static::Direction::Right:
		 worldY++;
		 playerBar->markerX += Global::playerMarkerWidth;
		 break;
	 case Static::Direction::Left:
		 worldY--;
		 playerBar->markerX -= Global::playerMarkerWidth;
		 break;
	 }
 }
 void Player::screenTransition(){
	 int minTransitionStep = 2;
	 float increaseStep = (float)(maxTransitionStep / minTransitionStep);
	 float viewX = Global::gameView.getCenter().x;
	 float viewY = Global::gameView.getCenter().y;
	 float nextXPosition=0;
	 float nextYPosition = 0;
	 switch (dir){

	 case Static::Direction::Right:
		 Global::gameView.setCenter(viewX + (float)((Global::roomWidth / (increaseStep))),
			 (float)(Global::roomHeight*worldX) +(Global::inventoryHeight/2) + (Global::roomHeight /  2));
		 nextXPosition = (float)minTransitionStep*Global::roomWidth / maxTransitionStep;
		 break;

	 case Static::Direction::Left:
		 Global::gameView.setCenter(viewX - (float)(Global::roomWidth / (increaseStep)),
			 (float)(Global::roomHeight*worldX) + (Global::inventoryHeight/2) + Global::roomHeight / 2);
		 nextXPosition = -((float)minTransitionStep*Global::roomWidth / maxTransitionStep);
		 break;

	 case Static::Direction::Down:
		 Global::gameView.setCenter(viewX, viewY + (Global::roomHeight / (increaseStep)));
		 nextYPosition = (float)minTransitionStep*Global::roomHeight / maxTransitionStep;
		 break;
	 case Static::Direction::Up:
		 Global::gameView.setCenter(viewX, viewY - (Global::roomHeight / (increaseStep)));
		 nextYPosition = -((float)minTransitionStep*Global::roomHeight / maxTransitionStep);
		 break;
	 }
	 playerBar->setBarNextPosition(nextXPosition,nextYPosition);
	 inventory->updateInventoryPosition(nextXPosition, nextYPosition);
	 walkingAnimation[walkAnimationIndex]->updateAnimationFrame(dir, position);
	 transitionStep -= minTransitionStep;
	 if (transitionStep == 0){
		 endScreenTransition();
	 }
 }
 int Player::getXOffset(){
	 xOffset = 0;
	if (dir == Static::Direction::Left)
		 xOffset = -Global::minStep;
	 else if (dir == Static::Direction::Right)
		 xOffset = Global::minStep;
	 return xOffset;
 }
 int Player::getYOffset(){
	 yOffset = 0;
	 if (dir == Static::Direction::Up)
		 yOffset = -Global::minStep;
	 else if (dir == Static::Direction::Down)
		 yOffset = Global::minStep;
	 return yOffset;
 }
 void Player::getUnalignedCount(Static::Direction nextDir){
	 int x = (int)position.x;
	 int y = (int)position.y;
	 stepIsNegative = false;
	 switch (nextDir){
	 case Static::Direction::Right:
		 stepToAlign = Global::HalfTileHeight -( y %Global::HalfTileHeight);
		 break;
	 case Static::Direction::Left:
		 stepToAlign = Global::HalfTileHeight - (y %Global::HalfTileHeight);
		 break;
	 case Static::Direction::Up:
		 stepToAlign = Global::HalfTileWidth - (x %Global::HalfTileWidth);
		 break;
	 case Static::Direction::Down:
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
	 case Static::Direction::Right:
	 case Static::Direction::Left:
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
	 case Static::Direction::Up:
	 case Static::Direction::Down:
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
	 walkingAnimation[walkAnimationIndex]->sprite.setPosition(position.x, position.y);
	 attackAnimation[attackAnimationIndex]->sprite.setPosition(position.x, position.y);
 }
 void Player::completeMove(){
	 stepToMove -= Global::minStep;
	 switch (dir){
	 case Static::Direction::Right:
		 position.x += Global::minStep;
			 break;
	 case Static::Direction::Left:
		 position.x -= Global::minStep;
			 break;
	 case Static::Direction::Up:
		 position.y -= Global::minStep;
			 break;
	 case Static::Direction::Down:
		 position.y += Global::minStep;
			 break;
	 }
	 walkingAnimation[walkAnimationIndex]->sprite.setPosition(position.x, position.y);
	 attackAnimation[attackAnimationIndex]->sprite.setPosition(position.x, position.y);
 }
 void Player::draw(sf::RenderWindow& mainWindow){
	 mainWindow.setView(Global::gameView);
	 playerBar->draw(mainWindow);
	 if (!isAttacking)
		 mainWindow.draw(walkingAnimation[walkAnimationIndex]->sprite);
	 else {
		 mainWindow.draw(attackAnimation[attackAnimationIndex]->sprite);
		 mainWindow.draw(sword->sprite);
		 mainWindow.draw(*(sword->fullMask));
	 }
	 drawText(mainWindow);
	 mainWindow.draw(*fullMask);
 }
 void Player::drawText(sf::RenderWindow& mainWindow){
	 sf::Font font;
	 std::stringstream pos;
	 pos << "X:" << position.x << std::endl << "Y:" << position.y << std::endl
		 <<"WorldX:"<<worldX <<std::endl <<"WorldY:"<<worldY<<std::endl;
	 font.loadFromFile("arial.ttf");
	 sf::Text txt(pos.str(), font);
	 txt.setColor(sf::Color::Red);
	 txt.setPosition(position.x, position.y - 64);
	 txt.setCharacterSize(12);
	 mainWindow.draw(txt);
 }