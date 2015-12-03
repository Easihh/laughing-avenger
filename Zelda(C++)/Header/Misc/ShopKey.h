#ifndef SHOPKEY_H
#define SHOPKEY_H
#include "Misc\ShopObject.h"
class ShopKey :public ShopObject {
public:
	ShopKey(Point pos);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
};
#endif