#ifndef THROWNBOMB_H
#define THROWNBOMB_H
#include "Misc\GameObject.h"
#include "Item\BombEffect.h"
#include "Utility\Static.h"
#include <SFML/Audio.hpp>
class ThrownBomb :public GameObject{
public:
	ThrownBomb(Point position,Direction direction);
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
private:
	int currentFrame, maxFrame = 90;
	void setup(Direction direction);
	void createBombEffect();
};
#endif