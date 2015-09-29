#include "Player.h"
#include <sstream>
#include <Windows.h>
#include <iostream>
 Player::Player(float x,float y){
	 xPosition = x * Global::TileWidth;
	 yPosition = y * Global::TileHeight;
	 width = Global::TileWidth;
	 height = Global::TileHeight;
	 dir = Static::Direction::Up;
	 canAttack = true;
	 isAttacking = false;
	 loadImage();
}
 Player::~Player(){
 }
 void Player::loadImage(){
	walkAnimation =new Animation("Link_Movement", width, height,xPosition,yPosition,6);
	attackAnimation=new Animation("Link_Attack", width, height, xPosition, yPosition, NULL);
 }
 void Player::update(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]){
	 bool keyPressed = false;
	 sword->update(isAttacking, canAttack);
	 if (sf::Keyboard::isKeyPressed(sf::Keyboard::Left)){
		 keyPressed = true;
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
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Right)){
		 keyPressed = true;
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
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Up)){
		 keyPressed = true;
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
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Down)){
		 keyPressed = true;
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
	 if (keyPressed)
		walkAnimation->updateAnimationFrame(dir);
	 attackAnimation->updateAnimationFrame(dir);
 }
 bool Player::isColliding(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]){
	 bool collision = false;
	 for (int i = 0; i < Static::WorldRows; i++){
		 for (int j = 0; j < Static::WorldColumns; j++){
			 if (worldLayer[i][j] == this)continue;
			 if (worldLayer[i][j] == NULL)continue;
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
	 position << "X:" << xPosition << std::endl << "Y:" << yPosition;
	 font.loadFromFile("arial.ttf");
	 sf::Text txt(position.str(), font);
	 txt.setColor(sf::Color::Red);
	 txt.setPosition(512, 200);
	 txt.setCharacterSize(24);
	 mainWindow.draw(txt);
 }