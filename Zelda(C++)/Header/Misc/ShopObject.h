#ifndef SHOPOBJECT_H
#define SHOPOBJECT_H
#include "Misc\GameObject.h"
class ShopObject :public GameObject {
public:
	bool isVisible,isObtained;
	int currentFrame;
	const int maxFrame = 75;
	Point origin;
	void resetShopItem();
	void hideOtherShopItems(std::vector<std::shared_ptr<GameObject>>* Worldmap);
private:
};
#endif