#include "Monster\DeathEffect.h"
#include "Utility\Static.h"
DeathEffect::~DeathEffect(){}
DeathEffect::DeathEffect(Point pos){
	position = pos;
	texture.loadFromFile("Tileset/kill_effect.png");
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void DeathEffect::update(std::vector<GameObject*>* worldMap){
	currentDuration++;
	if (currentDuration > maxDuration)
		Static::toDelete.push_back(this);
}
void DeathEffect::draw(sf::RenderWindow& window){
	window.draw(sprite);
}