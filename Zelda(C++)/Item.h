#ifndef ITEM_H
#define ITEM_H
#include "SFML\Graphics.hpp"
#include "GameObject.h"
class Item :public GameObject{
public:
	Item(float x,float y,std::string item);
	~Item();
	void update(GameObject* worldMap[Static::WorldRows][Static::WorldColumns]);
	void draw(sf::RenderWindow& mainWindow);
private:
	void loadImage();
	std::string itemName;
};
#endif