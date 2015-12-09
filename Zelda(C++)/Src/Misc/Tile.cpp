#include "Misc\Tile.h"
#include "Utility\Static.h"
#include <iostream>
#include "Misc\Flame.h"
Tile::Tile() {
}//used for Sub Class
Tile::Tile(Point pos, bool canBeCollidedWith, TileType type) {
	position = pos;
	width = Global::TileWidth;
	height = Global::TileHeight;
	isCollideable = canBeCollidedWith;
	hasBeenSetup = false;
	id = (int)type;
}
void Tile::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
	if (!hasBeenSetup){
		loadTileImage(id);
		setupFullMask();
		hasBeenSetup = true;
	}
}
void Tile::update(){}
void Tile::loadTileImage(int type){
	texture = Static::gameResource.at(type);
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}