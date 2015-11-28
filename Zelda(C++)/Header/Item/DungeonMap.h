#ifndef DUNGEONMAP_H
#define DUNGEONMAP_H
#include "Misc\GameObject.h"
class DungeonMap :public GameObject{
public:
	DungeonMap(Point pos);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
};
#endif