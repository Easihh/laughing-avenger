#ifndef POTIONPICKUP_H
#define POTIONPICKUP_H
#include "Misc\GameObject.h"
class PotionPickUp :public GameObject {
public:
	PotionPickUp(Point pos);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
	int currentFrame;
	const int maxFrame = 75;
	bool isObtained;
};

#endif