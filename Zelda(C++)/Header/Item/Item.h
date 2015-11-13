#ifndef ITEM_H
#define ITEM_H
#include "SFML\Graphics.hpp"
#include "Misc\GameObject.h"
#include "Utility\PlayerInfo.h"
class Item :public GameObject{
public:
	Item(Point position,std::string item);
	Item();
	virtual void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void draw(sf::RenderWindow& mainWindow);
	virtual void onUse(Point pos, std::vector<std::shared_ptr<GameObject>>* worldMap,Direction dir);
	bool isActive;
private:
	void loadImage();
	std::string itemName;
};
#endif