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
	void update(std::vector<GameObject*>* worldMap);
private:
	void loadAnimation();
	void movement(std::vector<GameObject*>* worldMap);
	bool isColliding(std::vector<GameObject*>* worldMap, std::unique_ptr<sf::RectangleShape>& mask, Point offsets);
	void pushBack(std::vector<GameObject*>* worldMap, Static::Direction swordDir);
	void takeDamage(int damage, std::vector<GameObject*>* worldMap, Static::Direction attackDir);
	void takeDamage(int damage);
	int getXOffset();
	int getYOffset();
	void pushbackUpdate();
	void getNextDirection(Static::Direction blockedDir);
	bool isOutsideRoomBound(Point pos);
	Animation * walkingAnimation[3];
	Static::Direction dir;
	const int minStep = 1;
	int getDistanceToMapBoundary(Static::Direction direction);
	int getMinimumLineCollisionDistance(Static::Direction direction, std::vector<GameObject*>* worldMap);
};
#endif