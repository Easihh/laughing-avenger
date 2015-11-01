#include "Monster\Monster.h"
Monster::Monster(){}
Monster::~Monster(){}
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
void Monster::takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap, Static::Direction swordDir) {}
void Monster::takeDamage(int damage){}
void Monster::updateMasks(){
	mask->setPosition(position.x+8, position.y+8);
	fullMask->setPosition(position.x, position.y);
}