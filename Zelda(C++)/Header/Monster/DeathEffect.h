#ifndef DEATHEFFECT_H
#define DEATHEFFECT_H
#include "Misc\GameObject.h"
class DeathEffect :public GameObject{
public:
	~DeathEffect();
	DeathEffect(Point position);
	void update(std::vector<GameObject*>* worldMap);
	void draw(sf::RenderWindow& mainWindow);
private:
	int currentDuration;
	const int maxDuration = 10;
};
#endif