#ifndef CANDLEFLAME_H
#define CANDLEFLAME_H
#include "Misc\GameObject.h"
#include "Animation.h"
class CandleFlame :public GameObject {
public:
	CandleFlame(Point pos, Direction dir);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
	std::unique_ptr<Animation> flameAnimation;
	void setupFlame();
	void updateFlameMovement();
	int currentFrame,flamePower,currentDuration;
	const int maxFrame = 40,maxDuration=120;
};
#endif

