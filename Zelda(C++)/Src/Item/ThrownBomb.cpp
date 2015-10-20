#include "Item\ThrownBomb.h"
#include <iostream>
ThrownBomb::~ThrownBomb(){}
ThrownBomb::ThrownBomb(float x, float y, Static::Direction direction){
	xPosition = x;
	yPosition = y;
	width = 32;
	height = 32;
	currentFrame = 0;
	setup(direction);
}
void ThrownBomb::setup(Static::Direction direction){
	texture.loadFromFile("tileset/Bomb.png");
	sprite.setTexture(texture);
	switch (direction)
	{
	case Static::Direction::Down:
		sprite.setPosition(xPosition, yPosition + height);
		break;
	case Static::Direction::Up:
		sprite.setPosition(xPosition, yPosition - height);
		break;
	case Static::Direction::Right:
		sprite.setPosition(xPosition + width, yPosition);
		break;
	case Static::Direction::Left:
		sprite.setPosition(xPosition - width, yPosition);
		break;
	}
}
void ThrownBomb::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
}
void ThrownBomb::update(std::vector<GameObject*>* worldMap){
	std::cout << "Update Thrown Bomb";
	currentFrame++;
	if (currentFrame > maxFrame){
		toBeDeleted = true;
	}
}