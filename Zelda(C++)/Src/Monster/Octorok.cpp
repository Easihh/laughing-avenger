#include "Monster\Octorok.h"
#include "Utility\Static.h"
#include <iostream>
#include "Monster\DeathEffect.h"
Octorok::Octorok(Point pos, bool canBeCollidedWith){
	spawnRow = position.x / Global::TileWidth;
	spawnCol = (position.y - Global::inventoryHeight) / Global::TileHeight;
	position = pos;
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
	mask->setPosition(position.x + 8, position.y + 8);
}
Octorok::~Octorok(){}
void Octorok::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
	mainWindow.draw(*mask);
	mainWindow.draw(*fullMask);
}
void Octorok::update(std::vector<GameObject*>* worldMap){
	if (healthPoint <= 0){
		Point pt(position.x + (width / 4), position.y + (height / 4));
		Static::toAdd.push_back(new DeathEffect(pt));
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
	sprite.setPosition(position.x, position.y);
}
