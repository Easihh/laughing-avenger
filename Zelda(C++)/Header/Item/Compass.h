#ifndef COMPASS_H
#define COMPASS_H
#include "Misc\GameObject.h"
class Compass :public GameObject{
public:
	Compass(Point pos);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
};
#endif