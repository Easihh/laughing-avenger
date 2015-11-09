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
	int strength;
	void update(bool& isAttacking, bool& canAttack, std::vector<std::shared_ptr<GameObject>>* worldMap, std::vector<std::unique_ptr<Animation>>* walkAnimation);
private:
	int swordCurrentFrame, swordDelay;
	const int swordMaxFrame=12, swordMaxDelay=8;
	bool isCollidingWithMonster(std::vector<std::shared_ptr<GameObject>>* worldMap);
	std::vector<std::shared_ptr<GameObject>> collidingMonsterList;
	Static::Direction swordDir;
	void updateMonster(std::vector<std::shared_ptr<GameObject>>* worldMap);
};
#endif