#ifndef SHOPCANDLE_H
#define SHOPCANDLE_H
#include "Misc\ShopObject.h"
class ShopCandle :public ShopObject {
public:
	ShopCandle(Point pos);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
};
#endif