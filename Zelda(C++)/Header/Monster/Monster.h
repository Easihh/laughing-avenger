#ifndef MONSTER_H
#define MONSTER_H

#include "Misc\GameObject.h"
#include "Utility\Static.h"
#include<memory>
class Monster :public GameObject{
protected:
	int healthPoint;
	const int maxInvincibleFrame = 20;
	int currentInvincibleFrame;
	void checkInvincibility();
	void updateMasks();
	void checkParalyzeStatus();
public:
	bool isInvincible;
	Monster();
	virtual void takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction swordDir);
	virtual void takeDamage(int damage);
	virtual void dropItemOnDeath();
	int strength, walkAnimIndex,currentParalyzeTime;
	std::unique_ptr<sf::RectangleShape> mask;
	void setupMonsterMask();
	bool isParalyzed;
	const int maxParalyzeTime = 90;
};
#endif