#ifndef BOWPICKUP_H
#define BOWPICKUP_H
#include "Misc\GameObject.h"
class BowPickUp :public GameObject {
public:
	BowPickUp(Point pos);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
	int currentFrame;
	const int maxFrame = 75;
	bool isObtained;
};

#endif