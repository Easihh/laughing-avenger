#ifndef ARMOS_H
#define ARMOR_H
#include "Monster.h"
#include "Misc\Animation.h"
class Armos :public Monster {
public:
	Armos(Point position, bool canBeCollidedWith);
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
private:
	void loadAnimation();
	void movement(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void processDeath(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction attackDir);
	void takeDamage(int damage);
	void dropItemOnDeath();
	int getXOffset();
	int getYOffset();
	void getNextDirection(Direction blockedDir);
	void tryToChangeDirection();
	std::vector<std::unique_ptr<Animation>> walkingAnimation;
	const int minStep = 1;
};
#endif