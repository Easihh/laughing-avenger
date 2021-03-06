#include "Item\ThrownBomb.h"
#include <iostream>
#include "Type\EffectType.h"
ThrownBomb::ThrownBomb(Point pos,Direction direction){
	position = pos;
	width = 32;
	height = 32;
	currentFrame = 0;
	setup(direction);
}
void ThrownBomb::setup(Direction direction){
	texture.loadFromFile("tileset/Bomb.png");
	sprite.setTexture(texture);
	switch (direction)
	{
	case Direction::Down:
		position.y += height;
		break;
	case Direction::Up:
		position.y -= height;
		break;
	case Direction::Right:
		position.x += width;
		break;
	case Direction::Left:
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
		Sound::playSound(GameSound::BombExplose);
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