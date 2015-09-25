#ifndef TILE_H
#define TILE_H

#include "GameObject.h"

class Tile :public GameObject{
public:
	Tile(float x, float y, bool canBeCollidedWith,int type);
	~Tile();
	void draw(sf::RenderWindow& mainWindow);
	void update();
	void loadTileImage(int type);
};

#endif