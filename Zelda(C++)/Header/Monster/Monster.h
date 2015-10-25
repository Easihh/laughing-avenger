#ifndef MONSTER_H
#define MONSTER_H

#include "Misc\GameObject.h"
#include "Utility\Static.h"
class Monster :public GameObject{
protected:
	int healthPoint;
	bool isInvincible;
	const int maxInvincibleFrame = 20;
	int currentInvincibleFrame, pushbackStep;
	void checkInvincibility();
	void updateMasks();
public:
	Monster();
	~Monster();
	virtual void takeDamage(int damage,std::vector<GameObject*>* worldMap,Static::Direction swordDir);
	int strength, walkAnimIndex;;
	sf::RectangleShape* mask;
};
#endif