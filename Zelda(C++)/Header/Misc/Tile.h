#ifndef TILE_H
#define TILE_H

#include "GameObject.h"
#include"Utility\TileType.h"
class Tile :public GameObject{
public:
	Tile(Point position, bool canBeCollidedWith, TileType type);
	~Tile();
	void draw(sf::RenderWindow& mainWindow);
	void update();
	void loadTileImage(TileType type);
};

#endif