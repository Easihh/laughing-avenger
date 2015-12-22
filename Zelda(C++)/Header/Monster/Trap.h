#ifndef TRAP_H
#define TRAP_H
#include "Monster.h"
#include "Misc\Animation.h"
class Trap :public Monster {
public:
	Trap(Point position, bool canBeCollidedWith);
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
private:
	void loadImage();
	void movement(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void processDeath(std::vector<std::shared_ptr<GameObject>>* worldMap);
	int getXOffset();
	int getYOffset();
	void TrapMovement();
	void setReturnDirection();
	const int maxDistance=160;
	bool isReturning,isActive;
	int currentDistance, moveSpeed;
};
#endif