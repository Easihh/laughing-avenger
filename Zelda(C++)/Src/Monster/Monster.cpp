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
void Monster::takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap,Direction swordDir) {}
void Monster::takeDamage(int damage){}
void Monster::dropItemOnDeath() {}
void Monster::processDeath(std::vector<std::shared_ptr<GameObject>>* worldMap) {}
void Monster::updateMasks(){
	mask->setPosition(position.x, position.y);
	fullMask->setPosition(position.x, position.y);
}