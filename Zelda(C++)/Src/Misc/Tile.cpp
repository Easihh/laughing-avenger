#include "Misc\Tile.h"
#include "Utility\Static.h"
Tile::Tile(Point pos, bool canBeCollidedWith, TileType type) {
	position = pos;
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
void Tile::update(){}
void Tile::loadTileImage(TileType type) {
	switch (type){
	case TileType::Sand:
		texture.loadFromFile("Tileset/Sand.png");
		break;
	case TileType::GreenTree:
		texture.loadFromFile("Tileset/Tree.png");
		break;
	case TileType::BlackTile:
		texture.loadFromFile("Tileset/BlackTile.png");
		break;
	case TileType::BrownBlockType1:
		texture.loadFromFile("Tileset/type1BrownBlock.png");
		break;
	}
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}