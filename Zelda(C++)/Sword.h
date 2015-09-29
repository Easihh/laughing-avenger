#ifndef SWORD_H
#define SWORD_H

#include "GameObject.h"
class Sword :public GameObject{
public:
	Sword(float playerX, float playerY, Static::Direction dir);
	~Sword();
	void loadImage(Static::Direction dir);
	void update(bool& isAttacking, bool& canAttack);
private:
	int swordCurrentFrame, swordMaxFrame, swordDelay, swordMaxDelay;
};
#endif