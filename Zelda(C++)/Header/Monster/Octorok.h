#ifndef OCTOROK_H
#define OCTOROK_H

#include "Monster.h"
#include "Misc\Animation.h"
#include <string>
class Octorok :public Monster{
public:
	Octorok(Point position, bool canBeCollidedWith);
	~Octorok();
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
private:
	void loadAnimation();
	void movement(std::vector<std::shared_ptr<GameObject>>* worldMap);
	bool isColliding(std::vector<std::shared_ptr<GameObject>>* worldMap, std::unique_ptr<sf::RectangleShape>& mask, Point offsets);
	void pushBack(std::vector<std::shared_ptr<GameObject>>* worldMap, Static::Direction swordDir);
	void takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap, Static::Direction attackDir);
	void takeDamage(int damage);
	int getXOffset();
	int getYOffset();
	void pushbackUpdate();
	void getNextDirection(Static::Direction blockedDir);
	std::vector<std::unique_ptr<Animation>> walkingAnimation;
	Static::Direction dir;
	const int minStep = 1;
	int getDistanceToMapBoundary(Static::Direction direction);
	int getMinimumLineCollisionDistance(Static::Direction direction, std::vector<std::shared_ptr<GameObject>>* worldMap);
};
#endif