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
void BombEffect::collisionWithMonster(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	for(auto& monst :*worldMap)
	{
		if (dynamic_cast<Monster*>(monst.get()))
			if (intersect(fullMask, monst->fullMask, Point(0, 0))){
				((Monster*)monst.get())->takeDamage(bombDmg);
			}
	}
}
void BombEffect::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	currentFrame++;
	if (eType == EffectType::BombFinishExplode)
		collisionWithMonster(worldMap);
	if (currentFrame == maxFrame){
		if (eType == EffectType::BombExplode){
			std::shared_ptr<GameObject> effect = std::make_shared<BombEffect>(position, EffectType::BombFinishExplode);
			Static::toAdd.push_back(effect);
		}
		destroyGameObject(worldMap);
	}
}
void BombEffect::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
}