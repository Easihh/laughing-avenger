#ifndef ITEM_H
#define ITEM_H
#include "SFML\Graphics.hpp"
#include "Misc\GameObject.h"
#include "Utility\PlayerInfo.h"
class Item :public GameObject{
public:
	Item(Point position,std::string item);
	Item();
	~Item();
	virtual void update(std::vector<GameObject*> worldMap);
	void draw(sf::RenderWindow& mainWindow);
	virtual void onUse(PlayerInfo info, std::vector<GameObject*>* worldMap);
	bool isActive;
private:
	void loadImage();
	std::string itemName;
};
#endif