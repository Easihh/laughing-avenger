#include "Player.h"
#include <sstream>
#include <Windows.h>
#include <iostream>
#include "Tile.h"
 Player::Player(float x,float y){
	 xPosition = x;
	 yPosition = y;
	 worldX = (int)(yPosition / Global::roomHeight);
	 worldY = (int)(xPosition / Global::roomWidth);
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
	 inventory->items[0][0] = new Item(0, 0,"MagicalBoomerang");
	 inventory->items[2][2] = new Item(0, 0, "Bomb");
	 inventoryKeyReleased = true;
}
 Player::~Player(){}
 void Player::loadImage(){
	walkAnimation =new Animation("Link_Movement", width, height,xPosition,yPosition,6);
	attackAnimation=new Animation("Link_Attack", width, height, xPosition, yPosition, NULL);
 }
 void Player::update(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]){
	 sword->update(isAttacking, canAttack, worldLayer);
	 checkMovementInput(worldLayer);
	 checkAttackInput();
	 checkInventoryInput();
	 if (stepToMove != 0)
		 completeMove();
	 if (stepToAlign != 0)
		 snapToGrid();
	 attackAnimation->updateAnimationFrame(dir);
	 if (!isScreenTransitioning)
		checkMapBoundaries();
	 else screenTransition();
	 if (isCollidingWithMonster(worldLayer))
		 takeDamage();
	 checkInvincible();
	 fullMask->setPosition(xPosition, yPosition);
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
		 if (canAttack && !isAttacking){
			 delete sword;
			 sword = new Sword(xPosition, yPosition, dir);
			 isAttacking = true;
			 canAttack = false;
		 }
	 }
 }
 void Player::checkMovementInput(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]){
	 bool movementKeyPressed = false;
	 if (sf::Keyboard::isKeyPressed(sf::Keyboard::Left) && !isScreenTransitioning){
		 movementKeyPressed = true;
		 if (stepToMove == 0 && !isAttacking){
			 if (dir != Static::Direction::Left){
				 getUnalignedCount(Static::Direction::Left);
				 dir = Static::Direction::Left;
				 walkAnimation->reset();
			 }
			 if (!isColliding(worldLayer) && stepToAlign == 0)
				 stepToMove = Global::minStep;
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Right) && !isScreenTransitioning){
		 movementKeyPressed = true;
		 if (stepToMove == 0 && !isAttacking){
			 if (dir != Static::Direction::Right){
				 getUnalignedCount(Static::Direction::Right);
				 dir = Static::Direction::Right;
				 walkAnimation->reset();
			 }
			 if (!isColliding(worldLayer) && stepToAlign == 0)
				 stepToMove = Global::minStep;
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Up) && !isScreenTransitioning){
		 movementKeyPressed = true;
		 if (stepToMove == 0 && !isAttacking){
			 if (dir != Static::Direction::Up){
				 getUnalignedCount(Static::Direction::Up);
				 dir = Static::Direction::Up;
				 walkAnimation->reset();
			 }
			 if (!isColliding(worldLayer) && stepToAlign == 0)
				 stepToMove = Global::minStep;
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Down) && !isScreenTransitioning){
		 movementKeyPressed = true;
		 if (stepToMove == 0 && !isAttacking){
			 if (dir != Static::Direction::Down){
				 getUnalignedCount(Static::Direction::Down);
				 dir = Static::Direction::Down;
				 walkAnimation->reset();
			 }
			 if (!isColliding(worldLayer) && stepToAlign == 0)
				 stepToMove = Global::minStep;
		 }
	 }
	 if (movementKeyPressed)
		 walkAnimation->updateAnimationFrame(dir);
 }
 bool Player::isCollidingWithMonster(GameObject* worldMap[Static::WorldRows][Static::WorldColumns]){
	 bool isColliding = false;
	 int startX = worldX*Global::roomRows;
	 int startY = worldY*Global::roomCols;
	 for (int i = startY; i < startY + Global::roomCols; i++){
		 for (int j = startX; j < startX + Global::roomRows; j++){
			 if (worldMap[i][j] != NULL){
				 if (dynamic_cast<Monster*>(worldMap[i][j]))
					 if (intersect(fullMask, ((Monster*)worldMap[i][j])->mask, 0, 0)){
						 isColliding = true;
						 collidingMonster = (Monster*)worldMap[i][j];
						 break;
					 }
			 }
		 }
	 }
	 return isColliding;
 }
 void Player::takeDamage(){
	 if (!isInvincible){
		 playerBar->decreaseCurrentHP(collidingMonster->strength);
		 pushback();
		 if (playerBar->getCurrentHP() <= 0)
			 std::cout << "I'm Dead";
		 else isInvincible = true;
	 }
 }
 void Player::pushback(){
	 switch (dir){
	 case Static::Direction::Down:
		 yPosition -= height * 2;
		 break;
	 case Static::Direction::Up:
		 yPosition += height * 2;
		 break;
	 case Static::Direction::Left:
		 xPosition += width * 2;
		 break;
	 case Static::Direction::Right:
		 xPosition -= width * 2;
		 break;
	 }
	 if (isAttacking)
		sword->endSword();
	 walkAnimation->sprite.setPosition(xPosition, yPosition);
	 attackAnimation->sprite.setPosition(xPosition, yPosition);
 }
 void Player::checkInvincible(){
	 if (isInvincible){
		 currentInvincibleFrame++;
		 if (currentInvincibleFrame >= maxInvincibleFrame){
			 isInvincible = false;
			 currentInvincibleFrame = 0;
		 }
	 }
 }
 void Player::checkMapBoundaries(){
	 bool outsideBoundary = false;
	 float markerX = playerBar->playerMarker.getPosition().x;
	 float markerY = playerBar->playerMarker.getPosition().y;

	 switch (dir){
	 case Static::Direction::Right:
		 if (xPosition + width > (Global::roomWidth*worldY) + Global::roomWidth){
			 worldY++;
			 playerBar->markerX += Global::playerMarkerWidth;
			 outsideBoundary = true;
		 }
		 break;
	 case Static::Direction::Left:
		 if (xPosition < (Global::roomWidth*worldY)){
			 worldY--;
			 playerBar->markerX -= Global::playerMarkerWidth;
			 outsideBoundary = true;
		 }
		 break;
	 case Static::Direction::Down:
		 if (yPosition + height > (Global::roomHeight*worldX)+Global::roomHeight+Global::inventoryHeight){
			 worldX++;
			 playerBar->markerY += Global::playerMarkerHeight;
			 outsideBoundary = true;
		 }
		 break;
	 case Static::Direction::Up:
		 if (yPosition < (Global::roomHeight*worldX) + Global::inventoryHeight){
			 worldX--;
			 playerBar->markerY -= Global::playerMarkerHeight;
			 outsideBoundary = true;
		 }
		 break;
	 }
	 if (outsideBoundary){
		 transitionStep = maxTransitionStep;
		 isScreenTransitioning = true;
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
	 walkAnimation->updateAnimationFrame(dir);
	 transitionStep -= minTransitionStep;
	 if (transitionStep == 0){
		 stepToMove = Global::TileHeight+1;
		 isScreenTransitioning = false;
	 }
 }
 bool Player::isColliding(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]){
	 int startX = worldX*Global::roomRows;
	 int startY = worldY*Global::roomCols;
	 bool collision = false;
	 for (int i = startY; i <startY + Global::roomCols; i++){
		 for (int j = startX; j < startX + Global::roomRows; j++){
			 if (worldLayer[i][j] == this)continue;
			 if (worldLayer[i][j] == NULL)continue;
			 if (dynamic_cast<Tile*>(worldLayer[i][j]))
				if (intersect(fullMask, worldLayer[i][j]->fullMask, getXOffset(), getYOffset()))
					collision = true;
		 }
	 }
	 return collision;
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
	 int x = (int)xPosition;
	 int y = (int)yPosition;
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
				 yPosition += stepToAlign;
			 else  yPosition += Global::minStep;
		 }
		 else 
		 {
			 if (stepToAlign<Global::minStep)
				 yPosition -= stepToAlign;
			 else  yPosition -= Global::minStep;
		 }
		 break;
	 case Static::Direction::Up:
	 case Static::Direction::Down:
		 if (!stepIsNegative){
			 if (stepToAlign < Global::minStep)
				 xPosition += stepToAlign;
			 else xPosition += Global::minStep;
		 }
		 else
		 {
			 if (stepToAlign < Global::minStep)
				 xPosition -= stepToAlign;
			 else xPosition -= Global::minStep;
		 }
		 break;
	 }
	 if (stepToAlign < Global::minStep)
		 stepToAlign = 0;
	 else stepToAlign -= Global::minStep;
	 walkAnimation->sprite.setPosition(xPosition, yPosition);
	 attackAnimation->sprite.setPosition(xPosition, yPosition);
 }
 void Player::completeMove(){
	 stepToMove -= Global::minStep;
	 switch (dir){
	 case Static::Direction::Right:
		 xPosition += Global::minStep;
			 break;
	 case Static::Direction::Left:
		 xPosition -= Global::minStep;
			 break;
	 case Static::Direction::Up:
		 yPosition -= Global::minStep;
			 break;
	 case Static::Direction::Down:
		 yPosition += Global::minStep;
			 break;
	 }
	 walkAnimation->sprite.setPosition(xPosition, yPosition);
	 attackAnimation->sprite.setPosition(xPosition, yPosition);
 }
 void Player::draw(sf::RenderWindow& mainWindow){
	 mainWindow.setView(Global::gameView);
	 playerBar->draw(mainWindow);
	 if (!isAttacking)
		mainWindow.draw(walkAnimation->sprite);
	 else {
		 mainWindow.draw(attackAnimation->sprite);
		 mainWindow.draw(sword->sprite);
		 mainWindow.draw(*(sword->fullMask));
	 }
	 drawText(mainWindow);
	 mainWindow.draw(*fullMask);
 }
 void Player::drawText(sf::RenderWindow& mainWindow){
	 sf::Font font;
	 std::stringstream position;
	 position << "X:" << xPosition << std::endl << "Y:" << yPosition <<std::endl 
		 <<"WorldX:"<<worldX <<std::endl <<"WorldY:"<<worldY<<std::endl;
	 font.loadFromFile("arial.ttf");
	 sf::Text txt(position.str(), font);
	 txt.setColor(sf::Color::Red);
	 txt.setPosition(xPosition, yPosition-64);
	 txt.setCharacterSize(12);
	 mainWindow.draw(txt);
 }