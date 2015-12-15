#ifndef WALLMASTERSPAWNCONTROLLER_H
#define WALLMASTERSPAWNCONTROLLER_H

#include "Misc\GameObject.h"
#include "Misc\Animation.h"
#include "Monster\WallMasterSpawner.h"
class WallMasterSpawnController :public GameObject{
public:
	WallMasterSpawnController(Point position);
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
	WallMasterSpawner findClosestSpawner();
private:
	void loadAnimation();
	WallMasterSpawner findClosestSpawner();
};
#endif