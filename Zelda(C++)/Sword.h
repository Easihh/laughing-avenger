#ifndef SWORD_H
#define SWORD_H

#include "GameObject.h"
#include <vector>
class Sword :public GameObject{
public:
	Sword(float playerX, float playerY, Static::Direction dir);
	~Sword();
	void loadImage(Static::Direction dir);
	void endSword();
	void update(bool& isAttacking, bool& canAttack, GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]);
private:
	int swordCurrentFrame, swordDelay,strength;
	const int swordMaxFrame=30, swordMaxDelay=16;
	bool isCollidingWithMonster(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]);
	std::vector<GameObject*> collidingMonsterList;
	void updateMonster();
};
#endif