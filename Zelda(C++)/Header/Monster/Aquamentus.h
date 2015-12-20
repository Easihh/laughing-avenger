#ifndef AQUAMENTUS_H
#define AQUAMENTUS_H

#include "Monster.h"
#include "Misc\Animation.h"
#include <string>
class Aquamentus :public Monster{
public:
	Aquamentus(Point position);
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
private:
	void loadAnimation();
	void movement(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction attackDir);
	void takeDamage(int damage);
	void dropItemOnDeath();
	void shoot(std::vector<std::shared_ptr<GameObject>>* worldMap);
	std::vector<std::unique_ptr<Animation>> walkingAnimation;
	const int minStep = 1, maxTimeSinceLastProjectile = 180;
	float const maxForwardDistance = 128;
	float currentForwardDistance;
	int timeSinceLastProjectile;
};
#endif