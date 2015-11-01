#include "Monster\DeathEffect.h"
#include "Utility\Static.h"
DeathEffect::~DeathEffect(){}
DeathEffect::DeathEffect(Point pos){
	position = pos;
	texture.loadFromFile("Tileset/kill_effect.png");
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
	currentDuration = 0;
}
void DeathEffect::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	currentDuration++;
	if(currentDuration > maxDuration)
		destroyGameObject(worldMap);
}
void DeathEffect::draw(sf::RenderWindow& window){
	window.draw(sprite);
}