#ifndef BOMB_H
#define BOMB_H
#include "Item.h"
class Bomb :public Item{
public:
	~Bomb();
	Bomb(float x,float y,std::string name);
	typedef Item super;
	void onUse(float x,float y);
	void draw(sf::RenderWindow& mainWindow);
private:
};
#endif