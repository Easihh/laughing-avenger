#ifndef SHOPMARKER_H
#define SHOPMARKER_H
#include "GameObject.h"
class ShopMarker:public GameObject {
public:
	~ShopMarker();
	ShopMarker(Point position);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
	bool isCollidingWithPlayer(std::vector<std::shared_ptr<GameObject>>* worldMap);
	std::shared_ptr<GameObject> player;
};
#endif