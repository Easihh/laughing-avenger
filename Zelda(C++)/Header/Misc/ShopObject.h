#ifndef SHOPOBJECT_H
#define SHOPOBJECT_H
#include "Misc\GameObject.h"
class ShopObject :public GameObject {
public:
	bool isVisible,isObtained;
	int currentFrame,itemPrice;
	const int maxFrame = 75;
	Point origin;
	void resetShopItem();
	void drawCost(sf::RenderWindow& mainWindow, int itemPrice);
	void hideOtherShopItems(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	sf::Text txt;
	std::string price;
	sf::Font font;
private:
};
#endif