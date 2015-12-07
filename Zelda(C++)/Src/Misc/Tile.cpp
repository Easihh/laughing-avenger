#include "Misc\Tile.h"
#include "Utility\Static.h"
Tile::Tile() {}//used for Sub Class
Tile::Tile(Point pos, bool canBeCollidedWith, TileType type) {
	position = pos;
	width = Global::TileWidth;
	height = Global::TileHeight;
	isCollideable = canBeCollidedWith;
	loadTileImage(type);
	setupFullMask();
}
void Tile::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
}
void Tile::update(){}
void Tile::loadTileImage(TileType type) {
	switch (type){
	case TileType::Sand:
		//texture.loadFromFile("Tileset/Sand.png");
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::GreenTree:
		//texture.loadFromFile("Tileset/Tree.png");
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::BlackTile:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::BrownBlockType1:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::BrownBlockType2:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile1:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile2:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile3:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile4:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile5:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile6:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile7:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile8:
		texture = Static::gameResource.at((int)type);
		depth = 999;
		break;
	case TileType::DungeonTile9:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile10:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile11:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile12:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile13:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile14:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile15:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile16:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile17:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile18:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile19:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile20:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile21:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile22:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile23:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile24:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile25:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile26:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile27:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile28:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile29:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::ArtifactRoomStair:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::ArtifactRoomWall:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile32:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile33:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile34:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile35:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile36:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile37:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile38:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile39:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile40:
		texture = Static::gameResource.at((int)type);
		depth = 999;
		break;
	case TileType::DungeonTile41:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile42:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile43:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile44:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile45:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile46:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile47:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile48:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile49:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile50:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile51:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile52:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile53:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile54:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile55:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile56:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile57:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile58:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile59:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile60:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile61:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile62:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile63:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile64:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile65:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile66:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile67:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile68:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile69:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile70:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile71:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile72:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile73:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile74:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile75:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile76:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile77:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile78:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile79:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile80:
		texture = Static::gameResource.at((int)type);
		depth = 999;
		break;
	case TileType::DungeonTile81:
		texture = Static::gameResource.at((int)type);
		depth = 999;
		break;
	case TileType::DungeonTile82:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile83:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile84:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::RedDungeonTile:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile86:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile87:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::DungeonTile88:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::OverworldTile1:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::OverworldTile2:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::OverworldTile3:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::OverworldTile4:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::OverworldTile5:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::OverworldTile6:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::OverworldTile7:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::OverworldTile8:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::OverworldTile9:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::OverworldTile10:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::OverworldTile11:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::OverworldTile12:
		texture = Static::gameResource.at((int)type);
		break;
	case TileType::GreenArmosStatue:
		texture = Static::gameResource.at((int)type);
		break;
	}
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}