#ifndef SHOPBOMB_H
#define SHOPBOMB_H
#include "Misc\ShopObject.h"
class ShopBomb :public ShopObject {
public:
	ShopBomb(Point pos);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
	const int bombPerPurchase = 8,bombPrice=20;
};
#endif