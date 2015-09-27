#include "Player.h"
#include <sstream>
#include <Windows.h>
#include <iostream>
 Player::Player(float x,float y){
	 swordMaxFrame = 30;
	 swordCurrentFrame = 0;
	 swordDelay = 0;
	 swordMaxDelay = 16;
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
	 if (isAttacking){
		 swordCurrentFrame++;
		 if (swordCurrentFrame >= swordMaxFrame){
			 isAttacking = false;
			 swordCurrentFrame = 0;
			 delete sword;
		 }
	 }
	 if (!isAttacking && !canAttack)
		 swordDelay++;
		 if (swordDelay >= swordMaxDelay){
			 swordDelay = 0;
			 canAttack = true;
		 }

	 if (sf::Keyboard::isKeyPressed(sf::Keyboard::Left)){
		 keyPressed = true;
		 if (stepToMove == 0){
			 if (dir != Static::Direction::Left){
				 dir = Static::Direction::Left;
				 walkAnimation->reset();
			 }
			 if (!isColliding(worldLayer))
				stepToMove = Global::minStep;
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Right)){
		 keyPressed = true;
		 if (stepToMove == 0){
			 if (dir != Static::Direction::Right){
				 dir = Static::Direction::Right;
				 walkAnimation->reset();
			 }
			 if (!isColliding(worldLayer))
				 stepToMove = Global::minStep;
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Up)){
		 keyPressed = true;
		 if (stepToMove == 0){
			 if (dir != Static::Direction::Up){
				 dir = Static::Direction::Up;
				 walkAnimation->reset();
			 }
			 if (!isColliding(worldLayer))
				 stepToMove = Global::minStep;
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Down)){
		 keyPressed = true;
		 if (stepToMove == 0){
			 if (dir != Static::Direction::Down){
				 dir = Static::Direction::Down;
				 walkAnimation->reset();
			 }
			 if (!isColliding(worldLayer))
				 stepToMove = Global::minStep;
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Space)){
		 if (canAttack && !isAttacking){
			 sword = new Sword(xPosition, yPosition, dir);
			 isAttacking = true;
			 canAttack = false;
		 }
	 }
	 if (stepToMove != 0)
		 completeMove();
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
 void Player::completeMove(){
	 stepToMove -= 2;
	 switch (dir){
	 case Static::Direction::Right:
			 xPosition += 2;
			 break;
	 case Static::Direction::Left:
			 xPosition -= 2;
			 break;
	 case Static::Direction::Up:
			 yPosition -= 2;
			 break;
	 case Static::Direction::Down:
			 yPosition += 2;
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