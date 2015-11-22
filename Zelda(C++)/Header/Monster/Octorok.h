#ifndef OCTOROK_H
#define OCTOROK_H

#include "Monster.h"
#include "Misc\Animation.h"
#include <string>
class Octorok :public Monster{
public:
	Octorok(Point position, bool canBeCollidedWith);
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
private:
	void loadAnimation();
	void movement(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap,Direction attackDir);
	void takeDamage(int damage);
	void dropItemOnDeath();
	int getXOffset();
	int getYOffset();
	void getNextDirection(Direction blockedDir);
	std::vector<std::unique_ptr<Animation>> walkingAnimation;
	const int minStep = 1;
};
#endif