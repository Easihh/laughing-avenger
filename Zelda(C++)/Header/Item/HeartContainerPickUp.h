#ifndef HEARTCONTAINERPICKUP_H
#define HEARTCONTAINERPICKUP_H
#include "Misc\GameObject.h"
class HeartContainerPickUp :public GameObject {
public:
	HeartContainerPickUp(Point pos);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
	int currentFrame;
	const int maxFrame = 75;
	bool isObtained;
};

#endif