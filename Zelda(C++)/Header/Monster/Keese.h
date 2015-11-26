#ifndef KEESE_H
#define KEESE_H
#include "Monster.h"
#include "Misc\Animation.h"
class Keese :public Monster {
public:
	Keese(Point position, bool canBeCollidedWith);
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
private:
	void loadAnimation();
	void movement(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction attackDir);
	void takeDamage(int damage);
	void dropItemOnDeath();
	int getXOffset();
	int getYOffset();
	int timeSinceLastTryDirectionChange;
	const int minimumTimeSinceLastDirectionChange = 180;
	void getNextDirection(Direction blockedDir);
	void tryToChangeDirection();
	std::unique_ptr<Animation> keeseAnimation;
	const int minStep = 1;
};
#endif