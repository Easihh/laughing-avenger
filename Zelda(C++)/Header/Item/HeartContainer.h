#ifndef HEARTCONTAINER_H
#define HEARTCONTAINER_H
#include "Misc\GameObject.h"
class HeartContainer :public GameObject {
public:
	HeartContainer(Point pos);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
	int currentFrame;
	const int maxFrame = 75;
	bool isObtained;
};

#endif