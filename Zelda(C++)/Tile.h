#ifndef TILE_H
#define TILE_H

#include "GameObject.h"

class Tile:public GameObject{
public:
	Tile(float x,float y);
	~Tile();
	void draw(sf::RenderWindow& mainWindow);
	void update();
};

#endif