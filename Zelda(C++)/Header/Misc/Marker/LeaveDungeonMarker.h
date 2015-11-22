#ifndef LEAVEDUNGEONMARKER_H
#define LEAVEDUNGEONMARKER_H
#include "Misc\GameObject.h"
class LeaveDungeonMarker :public GameObject {
public:
	LeaveDungeonMarker(Point position);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
};
#endif