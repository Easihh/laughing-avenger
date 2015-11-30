#ifndef TRIFORCE_H
#define TRIFORCE_H
#include "Misc\GameObject.h"
#include "Type\DungeonLevel.h"
class Triforce :public GameObject {
public:
	Triforce(Point pos);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
	int currentFrame;
	Point getReturnPointForPlayerLeavingDungeon(DungeonLevel level);
	const int maxFrame = 420;
	bool isObtained;
};

#endif