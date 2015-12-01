#ifndef SHOPARROW_H
#define SHOPARROW_H
#include "Misc\ShopObject.h"
class ShopArrow :public ShopObject {
public:
	ShopArrow(Point pos);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
};
#endif