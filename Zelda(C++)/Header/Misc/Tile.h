#ifndef TILE_H
#define TILE_H

#include "GameObject.h"
#include "Type\TileType.h"
class Tile :public GameObject{
public:
	Tile();
	Tile(Point position, bool canBeCollidedWith, TileType type);
	void draw(sf::RenderWindow& mainWindow);
	void update();
	void loadTileImage(int type);
	bool hasBeenSetup;
private:
	int id;
};


#endif