#ifndef MONSTER_H
#define MONSTER_H

#include "GameObject.h"
class Monster :public GameObject{
protected:
	int healthPoint;
	bool isInvincible;
	const int maxInvincibleFrame = 30;
	int currentInvincibleFrame;
	void checkInvincibility();
public:
	Monster();
	~Monster();
	void takeDamage(int damage);
	int strength;
	sf::RectangleShape* mask;
};
#endif