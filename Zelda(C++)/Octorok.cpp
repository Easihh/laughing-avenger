#include "Octorok.h"
#include <iostream>
Octorok::Octorok(float x, float y, bool canBeCollidedWith){
	xPosition = x;
	yPosition = y;
	width = Global::TileWidth;
	height = Global::TileHeight;
	isCollideable = canBeCollidedWith;
	loadImage("Tileset/redOctorok.png");
	isInvincible = false;
	healthPoint = 2;
	strength = 1;
	currentInvincibleFrame = 0;
}
Octorok::~Octorok(){}
void Octorok::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
}
void Octorok::update(GameObject* worldMap[Static::WorldRows][Static::WorldColumns]){
	if (healthPoint <= 0){
		destroy(worldMap);
		std::cout << "Octorok Destroyed";
	}
	else 
	{
		checkInvincibility();
	}
}
void Octorok::loadImage(std::string filename){
	texture.loadFromFile(filename);
	sprite.setTexture(texture);
	sprite.setPosition(xPosition, yPosition);
}
