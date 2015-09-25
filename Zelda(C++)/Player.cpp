#include "Player.h"
#include <sstream>
#include <Windows.h>
 Player::Player(float x,float y){
	 xPosition = x * Global::TileWidth;
	 yPosition = y * Global::TileHeight;
	 width = Global::TileWidth;
	 height = Global::TileHeight;
	 dir = Up;
	 walkAnimationIndex = 0;
	 loadImage();
}
 Player::~Player(){
 }
 void Player::loadImage(){
	 texture.loadFromFile("Tileset/Link_Movement.png");
	 subRect.height = height;
	 subRect.width = width;
	 subRect.left = 0;
	 subRect.top = 0;
	 sprite.setTexture(texture);
	 sprite.setTextureRect(subRect);
	 sprite.setPosition(xPosition, yPosition);
 }
 void Player::update(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]){
	 if (sf::Keyboard::isKeyPressed(sf::Keyboard::Left)){
		 if (stepToMove == 0){
			 if (dir != Left){
				 dir = Left;
				 walkAnimationIndex = 0;
				 updateAnimationFrame();
			 }
			 dir = Left;
			 if (!isColliding(worldLayer))
				stepToMove = minStep;
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Right)){
		 if (stepToMove == 0){
			 if (dir != Right){
				 dir = Right;
				 walkAnimationIndex = 0;
				 updateAnimationFrame();
			 }
			 dir = Right;
			 if (!isColliding(worldLayer))
				 stepToMove = minStep;
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Up)){
		 if (stepToMove == 0){
			 if (dir != Up){
				 dir = Up;
				 walkAnimationIndex = 0;
				 updateAnimationFrame();
			 }
			 dir = Up;
			 if (!isColliding(worldLayer))
				 stepToMove = minStep;
		 }
	 }
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Down)){
		 if (stepToMove == 0){
			 if (dir != Down){
				 dir = Down;
				 walkAnimationIndex = 0;
				 updateAnimationFrame();
			 }
			 dir = Down;
			 if (!isColliding(worldLayer))
				 stepToMove = minStep;
		 }
	 }
	 if (stepToMove!=0)
		completeMove();
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
	 if (stepToMove==0){
		 walkAnimationIndex++;
		 if (walkAnimationIndex > 1)
			 walkAnimationIndex = 0;
		 updateAnimationFrame();
	 }
	 sprite.setPosition(xPosition, yPosition);
 }
 void Player::updateAnimationFrame(){
	 subRect.height = height;
	 subRect.width = width;
	 subRect.left = walkAnimationIndex*width;
	 switch (dir)
	 {
	 case Player::Right:
			subRect.top = height * 3;
			break;
	 case Player::Left:
			subRect.top = height*2;
		 break;
	 case Player::Up:
			subRect.top = 0;
			break;
	 case Player::Down:
			subRect.top = height;
			break;
	 }
	 sprite.setTextureRect(subRect);
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