#ifndef DUNGEONMARKER_H
#define DUNGEONMARKER_H
#include "Misc\GameObject.h"
class DungeonMarker :public GameObject {
public:
	DungeonMarker(Point position);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
};
#endif