#include "Player.h"
#include "Static.h"
#include <sstream>
#include <Windows.h>
 Player::Player(){
	 xPosition = 64;
	 yPosition = 64;
	 width = 32;
	 height = 32;
	 dir = Up;
}
 Player::~Player(){
 }
 void Player::update(std::map<std::string, GameObject*> mapObjects){
	 if (sf::Keyboard::isKeyPressed(sf::Keyboard::Left)){
		 if (stepToMove == 0){
			 dir = Left;
			 if (!isColliding(mapObjects))
				stepToMove = minStep;
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Right)){
		 if (stepToMove == 0){
			 dir = Right;
			 if (!isColliding(mapObjects))
				 stepToMove = minStep;
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Up)){
		 if (stepToMove == 0){
			 dir = Up;
			 if (!isColliding(mapObjects))
				 stepToMove = minStep;
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Down)){
		 if (stepToMove == 0){
			 dir = Down;
			 if (!isColliding(mapObjects))
				 stepToMove = minStep;
		 }
	 }
	 if (stepToMove!=0)
		completeMove();
 }
 bool Player::isColliding(std::map<std::string, GameObject*> mapObjects){
	 collision = false;
	 std::map<std::string, GameObject*>::iterator itr = mapObjects.begin();
	 std::stringstream message;
	 while (itr != mapObjects.end()){
		 GameObject* obj = itr->second;
		 if (obj == this)itr++;
		 else
		 {
			 if (Static::intersect(this, obj, getXOffset(),getYOffset())){
				 //message << "Collision Detected" << std::endl;
				 collision = true;
				 //OutputDebugString(message.str().c_str());
			 }
			 itr++;
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
 }
 void Player::draw(sf::RenderWindow& mainWindow){
	 sprite.setSize(sf::Vector2f(width, height));
	 sprite.setOutlineColor(sf::Color::Blue);
	 sprite.setFillColor(sf::Color::Black);
	 sprite.setOutlineThickness(1);
	 sprite.setPosition(xPosition, yPosition);
	 mainWindow.draw(sprite);

	 sf::Font font;
	 std::stringstream position;
	 position << "X:" << xPosition << std::endl << "Y:" << yPosition;
	 font.loadFromFile("arial.ttf");
	 sf::Text txt(position.str(), font);
	 txt.setColor(sf::Color::Red);
	 txt.setPosition(400, 400);
	 txt.setCharacterSize(24);
	 mainWindow.draw(txt);
 }