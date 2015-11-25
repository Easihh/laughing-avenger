#include "Monster\Armos.h"
#include "Utility\Static.h"
#include <iostream>
#include "Monster\DeathEffect.h"
#include "Misc\Tile.h"
#include "Type\RupeeType.h"
#include "Item\RupeeDrop.h"
Armos::Armos(Point pos, bool canBeCollidedWith) {
	position = pos;
	width = Global::TileWidth;
	height = Global::TileHeight;
	isCollideable = canBeCollidedWith;
	loadAnimation();
	isInvincible = false;
	healthPoint = 2;
	strength = 1;
	currentInvincibleFrame = 0;
	pushbackStep = 0;
	setupFullMask();
	mask = std::make_unique<sf::RectangleShape>();
	mask->setFillColor(sf::Color::Transparent);
	sf::Vector2f size(16, 16);
	mask->setSize(size);
	mask->setOutlineColor(sf::Color::Blue);
	mask->setOutlineThickness(1);
	mask->setPosition(position.x + 8, position.y + 8);
	dir = Direction::None;
	getNextDirection(Direction::None);
	walkAnimIndex = 0;
}
void Armos::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(sprite);
	mainWindow.draw(*mask);
	mainWindow.draw(*fullMask);
}
void Armos::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
}
void Armos::dropItemOnDeath() {
}
void Armos::loadAnimation() {
	texture.loadFromFile("Tileset/greenArmos.png");
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void Armos::takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction attackDir) {

}
void Armos::takeDamage(int damage) {
}
void Armos::getNextDirection(Direction blockedDir) {

}
