#include "Item\BombEffect.h"
#include "Utility\Static.h"
#include "Monster\Monster.h"
BombEffect::~BombEffect(){}
BombEffect::BombEffect(Point pos, EffectType type){
	position = pos;
	currentFrame = 0;
	eType = type;
	if (eType == EffectType::BombExplode)
		texture.loadFromFile("tileset/Bomb_Effect1.png");
	else {
		texture.loadFromFile("tileset/Bomb_Effect2.png");
		currentFrame = 10;
	}
	sf::Vector2f size(Global::TileWidth, Global::TileHeight);
	fullMask = std::make_unique<sf::RectangleShape>();
	fullMask->setPosition(position.x, position.y);
	fullMask->setSize(size);
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void BombEffect::collisionWithMonster(std::vector<GameObject*>* worldMap){
	for each (GameObject* monst in *worldMap)
	{
		if (dynamic_cast<Monster*>(monst))
			if (intersect(fullMask, monst->fullMask, Point(0, 0))){
				((Monster*)monst)->takeDamage(bombDmg);
			}
	}
}
void BombEffect::update(std::vector<GameObject*>* worldMap){
	currentFrame++;
	if (eType == EffectType::BombFinishExplode)
		collisionWithMonster(worldMap);
	if (currentFrame == maxFrame){
		if (eType == EffectType::BombExplode){
			Static::toAdd.push_back(new BombEffect(position, EffectType::BombFinishExplode));
		}
		Static::toDelete.push_back(this);
	}
}
void BombEffect::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
}