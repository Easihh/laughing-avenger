#ifndef GEL_H
#define GEL_H

#include "Monster.h"
#include "Misc\Animation.h"
#include <string>
class Gel :public Monster{
public:
	Gel(Point position, bool canBeCollidedWith);
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
private:
	void movement(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction attackDir);
	void takeDamage(int damage);
	void processDeath(std::vector<std::shared_ptr<GameObject>>* worldMap);
	int getXOffset();
	int getYOffset();
	void getNextDirection(Direction blockedDir);
	void tryToChangeDirection();
	void loadAnimation();
	std::unique_ptr<Animation> walkingAnimation;
	const int minStep = 1;
};
#endif