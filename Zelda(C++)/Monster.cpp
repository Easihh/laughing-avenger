#include "Monster.h"

Monster::Monster(){}
Monster::~Monster(){}
void Monster::destroy(GameObject* worldMap[Static::WorldRows][Static::WorldColumns]){
	int worldX = xPosition / Global::roomWidth;
	int worldY = yPosition / Global::roomHeight;
	float startX = worldX*Global::roomRows;
	float startY = worldY*Global::roomCols;
	for (int i = startY; i < startY + Global::roomCols; i++){
		for (int j = startX; j <startX + Global::roomRows; j++){
			if (worldMap[i][j] == this){
				delete worldMap[i][j];
				worldMap[i][j] = NULL;
			}
		}
	}
}
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