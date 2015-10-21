#include "Misc\Tile.h"
#include "Utility\Static.h"
Tile::Tile(float x, float y,bool canBeCollidedWith,int type){
	xPosition = x;
	yPosition = y;
	width = Global::TileWidth;
	height = Global::TileHeight;
	isCollideable = canBeCollidedWith;
	loadTileImage(type);
	setupFullMask();
}
Tile::~Tile(){}
void Tile::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
}
void Tile::update(){
}
void Tile::loadTileImage(int type){
	switch (type){
	case 1:
		texture.loadFromFile("Tileset/Sand.png");
		break;
	case 2:
		texture.loadFromFile("Tileset/Tree.png");
		break;
	}
	sprite.setTexture(texture);
	sprite.setPosition(xPosition, yPosition);
}