#ifndef WALLMASTERSPAWNER_H
#define WALLMASTERSPAWNER_H
#include "Misc\GameObject.h"
class WallMasterSpawner :public GameObject{
public:
	WallMasterSpawner(Point position,Direction spawnDir);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void draw(sf::RenderWindow& mainWindow);
private:
};


#endif