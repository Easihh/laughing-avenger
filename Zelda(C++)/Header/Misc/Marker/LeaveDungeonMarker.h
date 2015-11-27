#ifndef LEAVEDUNGEONMARKER_H
#define LEAVEDUNGEONMARKER_H
#include "Misc\GameObject.h"
#include "Type\DungeonLevel.h"
class LeaveDungeonMarker :public GameObject {
public:
	LeaveDungeonMarker(Point position);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
	Point getReturnPointForPlayerLeavingDungeon(DungeonLevel level);
};
#endif