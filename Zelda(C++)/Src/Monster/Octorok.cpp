#include "Monster\Octorok.h"
#include "Utility\Static.h"
#include <iostream>
Octorok::Octorok(float x, float y, bool canBeCollidedWith){
	spawnRow = x / Global::TileWidth;
	spawnCol = (y-Global::inventoryHeight) / Global::TileHeight;
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
	setupFullMask();
	mask= new sf::RectangleShape();
	mask->setFillColor(sf::Color::Transparent);
	sf::Vector2f size(16, 16);
	mask->setSize(size);
	mask->setOutlineColor(sf::Color::Blue);
	mask->setOutlineThickness(1);
	mask->setPosition(xPosition + 8, yPosition + 8);
}
Octorok::~Octorok(){}
void Octorok::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
	mainWindow.draw(*mask);
	mainWindow.draw(*fullMask);
}
void Octorok::update(std::vector<GameObject*>* worldMap){
	if (healthPoint <= 0){
		Static::toDelete.push_back(this);
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
