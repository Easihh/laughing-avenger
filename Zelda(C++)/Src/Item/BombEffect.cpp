#include "Item\BombEffect.h"
#include "Utility\Static.h"
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
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void BombEffect::update(std::vector<GameObject*>* worldMap){
	currentFrame++;
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