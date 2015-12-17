#ifndef WALLMASTERSPAWNCONTROLLER_H
#define WALLMASTERSPAWNCONTROLLER_H

#include "Misc\GameObject.h"
#include "Misc\Animation.h"
class WallMasterSpawnController :public GameObject{
public:
	WallMasterSpawnController(Point position);
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
private:
	const int maxSpawn = 10,maxTimeSpawner=240;
	int currentSpawnCount,currentTimeSpawner;
};
#endif