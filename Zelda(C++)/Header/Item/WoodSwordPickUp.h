#ifndef WOODSWORDPICKUP_H
#define WOODSWORDPICKUP_H
#include "Misc\GameObject.h"
class WoodSwordPickUp :public GameObject {
public:
	WoodSwordPickUp(Point pos);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
	int currentFrame;
	const int maxFrame = 75;
	bool isObtained;
};

#endif