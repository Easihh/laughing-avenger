#ifndef THROWNBOMB_H
#define THROWNBOMB_H
#include "GameObject.h"
class ThrownBomb :public GameObject{
public:
	~ThrownBomb();
	ThrownBomb(float x, float y);
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<GameObject*> worldMap);
private:
};
#endif