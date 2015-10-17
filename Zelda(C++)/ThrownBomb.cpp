#include "ThrownBomb.h"
#include <iostream>
ThrownBomb::~ThrownBomb(){}
ThrownBomb::ThrownBomb(float x,float y){
	xPosition = x;
	yPosition = y;
	texture.loadFromFile("tileset/Bomb.png");
	sprite.setTexture(texture);
	sprite.setPosition(xPosition, yPosition);
}
void ThrownBomb::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
}
void ThrownBomb::update(std::vector<GameObject*> worldMap){
	std::cout << "Update Thrown Bomb";
}