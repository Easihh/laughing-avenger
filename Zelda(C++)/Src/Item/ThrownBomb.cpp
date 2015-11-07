#include "Item\ThrownBomb.h"
#include <iostream>
#include "Utility\EffectType.h"
ThrownBomb::~ThrownBomb(){}
ThrownBomb::ThrownBomb(Point pos, Static::Direction direction){
	position = pos;
	width = 32;
	height = 32;
	currentFrame = 0;
	setup(direction);
	if(!buffer.loadFromFile("Sound/bombBlow.wav"))
		std::cout << "Failed to load bombBlow.wav";
	bombBlow.setBuffer(buffer);
}
void ThrownBomb::setup(Static::Direction direction){
	texture.loadFromFile("tileset/Bomb.png");
	sprite.setTexture(texture);
	switch (direction)
	{
	case Static::Direction::Down:
		position.y += height;
		break;
	case Static::Direction::Up:
		position.y -= height;
		break;
	case Static::Direction::Right:
		position.x += width;
		break;
	case Static::Direction::Left:
		position.x -= width;
		break;
	}
	sprite.setPosition(position.x, position.y);
}
void ThrownBomb::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
}
void ThrownBomb::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	currentFrame++;
	if (currentFrame > maxFrame){
		createBombEffect();
		destroyGameObject(worldMap);
		Sound::playSound(BombExplose);
	}
}
void ThrownBomb::createBombEffect(){
	std::shared_ptr<GameObject> effect = std::make_shared<BombEffect>(Point(position.x, position.y), EffectType::BombExplode);
	Static::toAdd.push_back(effect);
	effect = std::make_shared<BombEffect>(Point(position.x + width, position.y), EffectType::BombExplode);
	Static::toAdd.push_back(effect);
	effect = std::make_shared<BombEffect>(Point(position.x - width, position.y), EffectType::BombExplode);
	Static::toAdd.push_back(effect);
	effect = std::make_shared<BombEffect>(Point(position.x - (width / 2), position.y - height), EffectType::BombExplode);
	Static::toAdd.push_back(effect);
	effect = std::make_shared<BombEffect>(Point(position.x + (width / 2), position.y - height), EffectType::BombExplode);
	Static::toAdd.push_back(effect);
	effect = std::make_shared<BombEffect>(Point(position.x - (width / 2), position.y + height), EffectType::BombExplode);
	Static::toAdd.push_back(effect);
	effect = std::make_shared<BombEffect>(Point(position.x + (width / 2), position.y + height), EffectType::BombExplode);
	Static::toAdd.push_back(effect);
}