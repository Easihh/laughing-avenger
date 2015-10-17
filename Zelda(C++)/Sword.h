#ifndef SWORD_H
#define SWORD_H

#include "GameObject.h"
#include <vector>
#include "Animation.h"
class Sword :public GameObject{
public:
	Sword(float playerX, float playerY, Static::Direction dir);
	~Sword();
	void loadImage(Static::Direction dir);
	void endSword();
	void update(bool& isAttacking, bool& canAttack, std::vector<GameObject*> worldMap, Animation* walkAnimation[3]);
private:
	int swordCurrentFrame, swordDelay,strength;
	const int swordMaxFrame=20, swordMaxDelay=16;
	bool isCollidingWithMonster(std::vector<GameObject*> worldMap);
	std::vector<GameObject*> collidingMonsterList;
	Static::Direction swordDir;
	void updateMonster();
};
#endif