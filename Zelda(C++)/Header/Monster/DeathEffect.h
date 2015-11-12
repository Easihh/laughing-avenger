#ifndef DEATHEFFECT_H
#define DEATHEFFECT_H
#include "Misc\GameObject.h"
class DeathEffect :public GameObject{
public:
	DeathEffect(Point position);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void draw(sf::RenderWindow& mainWindow);
private:
	int currentDuration;
	const int maxDuration = 10;
};
#endif