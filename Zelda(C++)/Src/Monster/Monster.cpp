#include "Monster\Monster.h"
Monster::Monster(){}
Monster::~Monster(){}
void Monster::takeDamage(int damage){
	if (!isInvincible){
		healthPoint -= damage;
		isInvincible = true;
	}	
}
void Monster::checkInvincibility(){
	if (isInvincible){
		currentInvincibleFrame++;
		if (currentInvincibleFrame >= maxInvincibleFrame){
			currentInvincibleFrame = 0;
			isInvincible = false;
		}
	}
}