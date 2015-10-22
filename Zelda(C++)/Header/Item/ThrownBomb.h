#ifndef THROWNBOMB_H
#define THROWNBOMB_H
#include "Misc\GameObject.h"
#include "Item\BombEffect.h"
#include "Utility\Static.h"
class ThrownBomb :public GameObject{
public:
	~ThrownBomb();
	ThrownBomb(Point position,Static::Direction direction);
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<GameObject*>* worldMap);
private:
	int currentFrame, maxFrame = 2*Global::FPS_RATE;
	void setup(Static::Direction direction);
	void createBombEffect();
	BombEffect* effect;
};
#endif