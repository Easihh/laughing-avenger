#include "Monster\Monster.h"
Monster::Monster(){
	depth = 1;
}
void Monster::checkInvincibility(){
	if (isInvincible){
		currentInvincibleFrame++;
		if (currentInvincibleFrame == (maxInvincibleFrame / 2))
			walkAnimIndex++;
		if (currentInvincibleFrame >= maxInvincibleFrame){
			currentInvincibleFrame = 0;
			isInvincible = false;
			walkAnimIndex = 0;			
		}
	}
}
void Monster::checkParalyzeStatus(){
	if (isParalyzed){
		currentParalyzeTime++;
		if (currentParalyzeTime > maxParalyzeTime){
			isParalyzed = false;
			currentParalyzeTime = 0;
		}
	}
}
void Monster::setupMonsterMask(){
	mask = std::make_unique<sf::RectangleShape>();
	mask->setFillColor(sf::Color::Transparent);
	sf::Vector2f size(16, 16);
	mask->setSize(size);
	mask->setOutlineColor(sf::Color::Blue);
	mask->setOutlineThickness(1);
	mask->setPosition(position.x + 8, position.y + 8);
}
void Monster::takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap,Direction swordDir) {}
void Monster::takeDamage(int damage){}
void Monster::dropItemOnDeath() {}
void Monster::updateMasks(){
	mask->setPosition(position.x+8, position.y+8);
	fullMask->setPosition(position.x, position.y);
}