#ifndef LEAVESHOPMARKER_H
#define LEAVESHOPMARKER_H
#include "Misc\GameObject.h"
class LeaveShopMarker :public GameObject {
public:
	LeaveShopMarker(Point position);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
};
#endif