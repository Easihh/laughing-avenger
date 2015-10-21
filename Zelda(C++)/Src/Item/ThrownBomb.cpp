#include "Item\ThrownBomb.h"
#include <iostream>
#include "Utility\EffectType.h"
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
		yPosition += height;
		break;
	case Static::Direction::Up:
		yPosition -= height;
		break;
	case Static::Direction::Right:
		xPosition += width;
		break;
	case Static::Direction::Left:
		xPosition -= width;
		break;
	}
	sprite.setPosition(xPosition, yPosition);
}
void ThrownBomb::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
}
void ThrownBomb::update(std::vector<GameObject*>* worldMap){
	currentFrame++;
	if (currentFrame > maxFrame){
		createBombEffect();
		Static::toDelete.push_back(this);
	}
}
void ThrownBomb::createBombEffect(){
	effect = new BombEffect(xPosition, yPosition,EffectType::BombExplode);
	Static::toAdd.push_back(effect);
	effect = new BombEffect(xPosition + width, yPosition, EffectType::BombExplode);
	Static::toAdd.push_back(effect);
	effect = new BombEffect(xPosition - width, yPosition, EffectType::BombExplode);
	Static::toAdd.push_back(effect);
	effect = new BombEffect(xPosition - (width / 2), yPosition - height, EffectType::BombExplode);
	Static::toAdd.push_back(effect);
	effect = new BombEffect(xPosition + (width / 2), yPosition - height, EffectType::BombExplode);
	Static::toAdd.push_back(effect);
	effect = new BombEffect(xPosition - (width / 2), yPosition + height, EffectType::BombExplode);
	Static::toAdd.push_back(effect);
	effect = new BombEffect(xPosition + (width / 2), yPosition + height, EffectType::BombExplode);
	Static::toAdd.push_back(effect);
}