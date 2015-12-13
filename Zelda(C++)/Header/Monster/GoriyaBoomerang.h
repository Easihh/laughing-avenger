#ifndef GORIYABOOMERANG_H
#define GORIYABOOMERANG_H
#include "Monster\Monster.h"
#include "Utility\Static.h"
#include "Misc\Animation.h"
#include <SFML/Audio.hpp>
class GoriyaBoomerang :public Monster {
public:
	GoriyaBoomerang(Point pos, Direction direction);
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
	bool isReturning, hasBeenBlocked;
private:
	void setup();
	void setupInitialPosition();
	Direction boomrangDir;
	void boomrangMovement();
	void setReturnDirection(std::vector<std::shared_ptr<GameObject>>* worldMap);
	const int boomrangSpeed = 3,maxDistance=Global::roomWidth/4;
	int currentDistance;
	bool returnDirectionIsSet;
	void checkIfPlayerCanBlock(std::vector<std::shared_ptr<GameObject>>* worldMap);
	std::unique_ptr<Animation> boomrangAnimation;
};
#endif