#ifndef SHOPMARKER_H
#define SHOPMARKER_H
#include "Misc\GameObject.h"
class ShopMarker:public GameObject {
public:
	ShopMarker(Point position);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
};
#endif