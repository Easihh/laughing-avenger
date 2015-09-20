#ifndef BLOCK_H
#define BLOCK_H

#include "GameObject.h"

class Block:public GameObject{
public:
	Block();
	~Block();
	void draw(sf::RenderWindow& mainWindow);
	void update();
	sf::RectangleShape sprite;
};

#endif