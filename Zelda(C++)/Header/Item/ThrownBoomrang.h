#ifndef THROWNBOOMRANG_H
#define THROWNBOOMRANG_H
#include "Misc\GameObject.h"
#include "Utility\Static.h"
#include "Misc\Animation.h"
#include <SFML/Audio.hpp>
class ThrownBoomrang :public GameObject {
public:
	ThrownBoomrang(Point pos, Direction direction);
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
private:
	void setup();
	void setupInitialPosition();
	Direction boomrangDir;
	void boomrangMovement();
	void setCorrectDirection();
	void setDiagonalSpeed();
	bool isReturning;
	const int boomrangSpeed = 6;
	int stepsXToPlayer, stepsYToPlayer;
	float diagonalXspeed,diagonalYspeed;
	std::unique_ptr<Animation> boomrangAnimation;
};
#endif