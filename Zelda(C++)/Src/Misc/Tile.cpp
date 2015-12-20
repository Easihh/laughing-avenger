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
	//upper part of dungeon doors must have higher priority over the player in drawing. 
	if (type == TileType::DungeonTile8
		|| type == TileType::DungeonTile40
		|| type == TileType::DungeonTile80
		|| type == TileType::DungeonTile81
		)
		depth = 999;
	if (type == TileType::DungeonTile2
		||type == TileType::DungeonTile3
		|| type == TileType::DungeonTile5
		|| type == TileType::DungeonTile7
		||type == TileType::DungeonTile34
		|| type == TileType::DungeonTile35
		|| type == TileType::DungeonTile37
		|| type == TileType::DungeonTile38
		|| type == TileType::DungeonTile39
		|| type == TileType::DungeonTile74
		|| type == TileType::DungeonTile75
		|| type == TileType::DungeonTile77
		|| type == TileType::DungeonTile78
		|| type == TileType::DungeonTile82
		|| type == TileType::DungeonTile83
		|| type == TileType::DungeonTile84
		|| type == TileType::DungeonTile86
		|| type == TileType::DungeonTile87
		){
		depth = 2000;//higher depth than WallMaster so the WallMaster get covered by these tile.
	}
	id = (int)type;
}
void Tile::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
	if (!hasBeenSetup){
		loadTileImage(id);
		setupMask(&fullMask, width, height, sf::Color::Magenta);
		hasBeenSetup = true;
	}
}
void Tile::update(){}
void Tile::loadTileImage(int type){
	texture = Static::gameResource.at(type);
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}