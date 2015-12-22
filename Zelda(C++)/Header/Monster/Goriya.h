#ifndef GORIYA_H
#define GORIYA_H

#include "Monster.h"
#include "Misc\Animation.h"
#include <string>
#include "Monster\GoriyaBoomerang.h"
class Goriya :public Monster{
public:
	Goriya(Point position, bool canBeCollidedWith);
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
private:
	void loadAnimation();
	void movement(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction attackDir);
	void takeDamage(int damage);
	void processDeath(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void dropItemOnDeath();
	int getXOffset();
	int getYOffset();
	void getNextDirection(Direction blockedDir);
	void tryToChangeDirection();
	void shootBoomerang();
	std::shared_ptr<GoriyaBoomerang> myBoomerang;
	int timeSinceLastProjectileDecision;
	std::vector<std::unique_ptr<Animation>> walkingAnimation;
	const int minStep = 1,minTimeBetweenProjectile=120;
	bool projectileIsActive;
};
#endif