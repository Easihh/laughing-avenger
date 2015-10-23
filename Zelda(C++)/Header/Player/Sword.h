#ifndef SWORD_H
#define SWORD_H

#include "Misc\GameObject.h"
#include <vector>
#include "Misc\Animation.h"
class Sword :public GameObject{
public:
	Sword(Point pos, Static::Direction dir);
	~Sword();
	void loadImage(Static::Direction dir);
	void endSword();
	void update(bool& isAttacking, bool& canAttack,const std::vector<GameObject*>* worldMap, Animation* walkAnimation[3]);
private:
	int swordCurrentFrame, swordDelay,strength;
	const int swordMaxFrame=20, swordMaxDelay=16;
	bool isCollidingWithMonster(const std::vector<GameObject*>* worldMap);
	std::vector<GameObject*> collidingMonsterList;
	Static::Direction swordDir;
	void updateMonster();
};
#endif