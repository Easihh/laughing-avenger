#ifndef BOMBEFFECT_H
#define BOMBEFFECT_H
#include "Misc\GameObject.h"
class BombEffect:public GameObject{
public:
	~BombEffect();
	BombEffect();
	virtual void update(std::vector<GameObject*> worldMap);
	virtual void draw(sf::RenderWindow& mainWindow);
private:
};
#endif