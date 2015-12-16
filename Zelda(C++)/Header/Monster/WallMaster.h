#ifndef WALLMASTER_H
#define WALLMASTER_H

#include "Monster.h"
#include "Misc\Animation.h"
class WallMaster :public Monster{
public:
	WallMaster(Point position,Direction dir);
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
private:
	void loadAnimation();
	void setMaxDistance(Direction spawnDir);
	void setDirection(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void movement(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void processDeath(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void checkCollisions(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void updateAnimation();
	void takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction attackDir);
	void takeDamage(int damage);
	void dropItemOnDeath();
	int getXOffset();
	int getYOffset();
	int maxXDistance, maxYDistance;
	std::vector<std::unique_ptr<Animation>> walkingAnimation;
	const int minStep = 1;
	bool hasCaughtPlayer;
	void movePlayerToDungeonEntrance(std::vector<std::shared_ptr<GameObject>>* worldMap);
};
#endif