#ifndef ITEM_H
#define ITEM_H
#include "SFML\Graphics.hpp"
#include "GameObject.h"
#include "PlayerInfo.h"
class Item :public GameObject{
public:
	Item(float x,float y,std::string item);
	Item();
	~Item();
	void update(GameObject* worldMap[Static::WorldRows][Static::WorldColumns]);
	void draw(sf::RenderWindow& mainWindow);
	virtual void onUse(PlayerInfo info);
	bool isActive;
private:
	void loadImage();
	std::string itemName;
};
#endif