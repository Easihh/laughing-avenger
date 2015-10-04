#include "Player.h"
#include <sstream>
#include <Windows.h>
#include <iostream>
#include "Tile.h"
 Player::Player(float x,float y){
	 xPosition = x;
	 yPosition = y;
	 worldX = yPosition / Global::roomHeight;
	 worldY = xPosition / Global::roomWidth;
	 width = Global::TileWidth;
	 height = Global::TileHeight;
	 dir = Static::Direction::Up;
	 healthPoint = 2;
	 canAttack = true;
	 isAttacking = false;
	 isScreenTransitioning = false;
	 isInvincible = false;
	 setupPlayerBar();
	 loadImage();
}
 Player::~Player(){}
 void Player::setupPlayerMarker(){ 
	 playerMarker.setFillColor(sf::Color::Green);
	 sf::Vector2f size(Global::playerMarkerWidth, Global::playerMarkerHeight);
	 playerMarker.setSize(size);
	 playerMarker.setPosition(48, 32);
 }
 void Player::setupMap(){
	 overworldMap.setFillColor(sf::Color(128, 128, 128));
	 sf::Vector2f size(128,64);
	 overworldMap.setSize(size);
	 overworldMap.setPosition(48, 32);
 }
void Player::setupPlayerBar(){
	playerBar.setFillColor(sf::Color::Black);
	sf::Vector2f size(Global::roomWidth, Global::inventoryHeight);
	playerBar.setSize(size);
	setupMap();
	setupPlayerMarker();
 }
 void Player::loadImage(){
	walkAnimation =new Animation("Link_Movement", width, height,xPosition,yPosition,6);
	attackAnimation=new Animation("Link_Attack", width, height, xPosition, yPosition, NULL);
 }
 void Player::update(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]){
	 bool movementKeyPressed = false;
	 sword->update(isAttacking, canAttack, worldLayer);
	 if (sf::Keyboard::isKeyPressed(sf::Keyboard::Left) && !isScreenTransitioning){
		 movementKeyPressed = true;
		 if (stepToMove == 0 && !isAttacking){
			 if (dir != Static::Direction::Left){
				 getUnalignedCount(Static::Direction::Left);
				 dir = Static::Direction::Left;
				 walkAnimation->reset();
			 }
			 if (!isColliding(worldLayer) && stepToAlign==0)
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
			 if (!isColliding(worldLayer) && stepToAlign==0)
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
			 if (!isColliding(worldLayer) && stepToAlign==0)
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
			 if (!isColliding(worldLayer) && stepToAlign==0)
				 stepToMove = Global::minStep;
		 }
	 }
	 if (sf::Keyboard::isKeyPressed(sf::Keyboard::Space)){
		 if (canAttack && !isAttacking){
			 delete sword;
			 sword = new Sword(xPosition, yPosition, dir);
			 isAttacking = true;
			 canAttack = false;
		 }
	 }
	 if (stepToMove != 0)
		 completeMove();
	 if (stepToAlign != 0)
		 snapToGrid();
	 if (movementKeyPressed)
		walkAnimation->updateAnimationFrame(dir);
	 attackAnimation->updateAnimationFrame(dir);
	 if (!isScreenTransitioning)
		checkMapBoundaries();
	 else screenTransition();
	 if (isCollidingWithMonster(worldLayer))
		 takeDamage();
	 checkInvincible();
 }
 bool Player::isCollidingWithMonster(GameObject* worldMap[Static::WorldRows][Static::WorldColumns]){
	 bool isColliding = false;
	 float startX = worldX*Global::roomRows;
	 float startY = worldY*Global::roomCols;
	 for (int i = startY; i < startY + Global::roomCols; i++){
		 for (int j = startX; j < startX + Global::roomRows; j++){
			 if (worldMap[i][j] != NULL){
				 if (dynamic_cast<Monster*>(worldMap[i][j]))
					 if (intersect(this, worldMap[i][j], 0, 0)){
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
		 healthPoint -= collidingMonster->strength;
		 pushback();
		 if (healthPoint <= 0)
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
	 float markerX = playerMarker.getPosition().x;
	 float markerY = playerMarker.getPosition().y;

	 switch (dir){
	 case Static::Direction::Right:
		 if (xPosition + width > (Global::roomWidth*worldY) + Global::roomWidth){
			 worldY++;
			 playerMarker.setPosition(markerX + Global::playerMarkerWidth, markerY);
			 outsideBoundary = true;
		 }
		 break;
	 case Static::Direction::Left:
		 if (xPosition < (Global::roomWidth*worldY)){
			 worldY--;
			 playerMarker.setPosition(markerX - Global::playerMarkerWidth, markerY);
			 outsideBoundary = true;
		 }
		 break;
	 case Static::Direction::Down:
		 if (yPosition + height > (Global::roomHeight*worldX)+Global::roomHeight+Global::inventoryHeight){
			 worldX++;
			 playerMarker.setPosition(markerX, markerY + Global::playerMarkerHeight);
			 outsideBoundary = true;
		 }
		 break;
	 case Static::Direction::Up:
		 if (yPosition < (Global::roomHeight*worldX) + Global::inventoryHeight){
			 worldX--;
			 playerMarker.setPosition(markerX, markerY - Global::playerMarkerHeight);
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
	 float increaseStep = maxTransitionStep / minTransitionStep;
	 float viewX = Global::gameView.getCenter().x;
	 float viewY = Global::gameView.getCenter().y;
	 float barX = playerBar.getPosition().x;
	 float barY = playerBar.getPosition().y;
	 float mapX = overworldMap.getPosition().x;
	 float mapY = overworldMap.getPosition().y;
	 float markerX = playerMarker.getPosition().x;
	 float markerY = playerMarker.getPosition().y;
	 float nextXPosition=0;
	 float nextYPosition = 0;
	 switch (dir){

	 case Static::Direction::Right:
		 Global::gameView.setCenter(viewX + (Global::roomWidth / (increaseStep)),
			 (Global::roomHeight*worldX) +(Global::inventoryHeight/2) + (Global::roomHeight /  2));
		 nextXPosition = (float)minTransitionStep*Global::roomWidth / maxTransitionStep;
		 break;

	 case Static::Direction::Left:
		 Global::gameView.setCenter(viewX - (Global::roomWidth / (increaseStep)),
			 (Global::roomHeight*worldX) + (Global::inventoryHeight/2) + Global::roomHeight / 2);
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
	 playerBar.setPosition(barX + nextXPosition, barY + nextYPosition);
	 overworldMap.setPosition(mapX + nextXPosition, mapY + nextYPosition);
	 playerMarker.setPosition(markerX + nextXPosition, markerY + nextYPosition);
	 walkAnimation->updateAnimationFrame(dir);
	 transitionStep -= minTransitionStep;
	 if (transitionStep == 0){
		 stepToMove = Global::TileHeight+1;
		 isScreenTransitioning = false;
	 }
 }
 bool Player::isColliding(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]){
	 float startX = worldX*Global::roomRows;
	 float startY = worldY*Global::roomCols;
	 bool collision = false;
	 for (int i = startY; i <startY + Global::roomCols; i++){
		 for (int j = startX; j < startX + Global::roomRows; j++){
			 if (worldLayer[i][j] == this)continue;
			 if (worldLayer[i][j] == NULL)continue;
			 if (dynamic_cast<Tile*>(worldLayer[i][j]))
				if (intersect(this, worldLayer[i][j], getXOffset(), getYOffset()))
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
	 int x = xPosition;
	 int y = yPosition;
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
	 mainWindow.draw(playerBar);
	 mainWindow.draw(overworldMap);
	 mainWindow.draw(playerMarker);
	 if (!isAttacking)
		mainWindow.draw(walkAnimation->sprite);
	 else {
		 mainWindow.draw(attackAnimation->sprite);
		 mainWindow.draw(sword->sprite);
	 }
	 drawText(mainWindow);
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