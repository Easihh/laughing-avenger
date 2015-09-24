#include "Player.h"
#include <sstream>
#include <Windows.h>
 Player::Player(float x,float y){
	 xPosition = x;
	 yPosition = y;
	 dir = Up;
	 loadImage();
}
 Player::~Player(){
 }
 void Player::loadImage(){
	 texture.loadFromFile("Tileset/Hero.png");
	 sprite.setTexture(texture);
	 sprite.setPosition(xPosition, yPosition);
 }
 void Player::update(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]){
	 if (sf::Keyboard::isKeyPressed(sf::Keyboard::Left)){
		 if (stepToMove == 0){
			 dir = Left;
			 if (!isColliding(worldLayer))
				stepToMove = minStep;
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Right)){
		 if (stepToMove == 0){
			 dir = Right;
			 if (!isColliding(worldLayer))
				 stepToMove = minStep;
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Up)){
		 if (stepToMove == 0){
			 dir = Up;
			 if (!isColliding(worldLayer))
				 stepToMove = minStep;
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Down)){
		 if (stepToMove == 0){
			 dir = Down;
			 if (!isColliding(worldLayer))
				 stepToMove = minStep;
		 }
	 }
	 if (stepToMove!=0)
		completeMove();
 }
 bool Player::isColliding(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]){
	 collision = false;
	 for (int i = 0; i < Static::WorldRows; i++){
		 for (int j = 0; j < Static::WorldColumns; j++){
			 if (worldLayer[i][j] == this)continue;
			 if (intersect(this, worldLayer[i][j], getXOffset(), getYOffset()))
				 collision = true;
		 }
	 }
	 return collision;
 }
 int Player::getXOffset(){
	 xOffset = 0;
	 if (dir == Left)
		 xOffset = -minStep;
	 else if (dir == Right)
		 xOffset = minStep;
	 return xOffset;
 }
 int Player::getYOffset(){
	 yOffset = 0;
	 if (dir == Up)
		 yOffset = -minStep;
	 else if (dir == Down)
		 yOffset = minStep;
	 return yOffset;
 }
 void Player::completeMove(){
	 stepToMove -= 2;
	 switch (dir){
		 case Right:
			 xPosition += 2;
			 break;
		 case Left:
			 xPosition -= 2;
			 break;
		 case Up:
			 yPosition -= 2;
			 break;
		 case Down:
			 yPosition += 2;
			 break;
	 }
	 sprite.setPosition(xPosition, yPosition);
 }
 void Player::draw(sf::RenderWindow& mainWindow){
	 mainWindow.draw(sprite);

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