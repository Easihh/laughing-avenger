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
	case TileType::BrownBlockType2:
		texture.loadFromFile("Tileset/type2BrownBlock.png");
		break;
	case TileType::DungeonTile1:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile1.png");
		break;
	case TileType::DungeonTile2:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile2.png");
		break;
	case TileType::DungeonTile3:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile3.png");
		break;
	case TileType::DungeonTile4:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile4.png");
		break;
	case TileType::DungeonTile5:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile5.png");
		break;
	case TileType::DungeonTile6:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile6.png");
		break;
	case TileType::DungeonTile7:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile7.png");
		break;
	case TileType::DungeonTile8:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile8.png");
		break;
	case TileType::DungeonTile9:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile9.png");
		break;
	case TileType::DungeonTile10:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile10.png");
		break;
	case TileType::DungeonTile11:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile11.png");
		break;
	case TileType::DungeonTile12:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile12.png");
		break;
	case TileType::DungeonTile13:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile13.png");
		break;
	case TileType::DungeonTile14:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile14.png");
		break;
	case TileType::DungeonTile15:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile15.png");
		break;
	case TileType::DungeonTile16:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile16.png");
		break;
	case TileType::DungeonTile17:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile17.png");
		break;
	case TileType::DungeonTile18:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile18.png");
		break;
	case TileType::DungeonTile19:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile19.png");
		break;
	case TileType::DungeonTile20:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile20.png");
		break;
	case TileType::DungeonTile21:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile21.png");
		break;
	case TileType::DungeonTile22:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile22.png");
		break;
	case TileType::DungeonTile23:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile23.png");
		break;
	case TileType::DungeonTile24:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile24.png");
		break;
	case TileType::DungeonTile25:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile25.png");
		break;
	case TileType::DungeonTile26:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile26.png");
		break;
	case TileType::DungeonTile27:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile27.png");
		break;
	case TileType::DungeonTile28:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile28.png");
		break;
	case TileType::DungeonTile29:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile29.png");
		break;
	case TileType::DungeonTile30:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile30.png");
		break;
	case TileType::DungeonTile31:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile31.png");
		break;
	case TileType::DungeonTile32:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile32.png");
		break;
	case TileType::DungeonTile33:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile33.png");
		break;
	case TileType::DungeonTile34:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile34.png");
		break;
	case TileType::DungeonTile35:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile35.png");
		break;
	case TileType::DungeonTile36:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile36.png");
		break;
	case TileType::DungeonTile37:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile37.png");
		break;
	case TileType::DungeonTile38:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile38.png");
		break;
	case TileType::DungeonTile39:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile39.png");
		break;
	case TileType::DungeonTile40:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile40.png");
		break;
	case TileType::DungeonTile41:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile41.png");
		break;
	case TileType::DungeonTile42:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile42.png");
		break;
	case TileType::DungeonTile43:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile43.png");
		break;
	case TileType::DungeonTile44:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile44.png");
		break;
	case TileType::DungeonTile45:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile45.png");
		break;
	case TileType::DungeonTile46:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile46.png");
		break;
	case TileType::DungeonTile47:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile47.png");
		break;
	case TileType::DungeonTile48:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile48.png");
		break;
	case TileType::DungeonTile49:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile49.png");
		break;
	case TileType::DungeonTile50:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile50.png");
		break;
	case TileType::DungeonTile51:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile51.png");
		break;
	case TileType::DungeonTile52:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile52.png");
		break;
	case TileType::DungeonTile53:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile53.png");
		break;
	case TileType::DungeonTile54:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile54.png");
		break;
	case TileType::DungeonTile55:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile55.png");
		break;
	case TileType::DungeonTile56:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile56.png");
		break;
	case TileType::DungeonTile57:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile57.png");
		break;
	case TileType::DungeonTile58:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile58.png");
		break;
	case TileType::DungeonTile59:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile59.png");
		break;
	case TileType::DungeonTile60:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile60.png");
		break;
	case TileType::DungeonTile61:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile61.png");
		break;
	case TileType::DungeonTile62:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile62.png");
		break;
	case TileType::DungeonTile63:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile63.png");
		break;
	case TileType::DungeonTile64:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile64.png");
		break;
	case TileType::DungeonTile65:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile65.png");
		break;
	case TileType::DungeonTile66:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile66.png");
		break;
	case TileType::DungeonTile67:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile67.png");
		break;
	case TileType::DungeonTile68:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile68.png");
		break;
	case TileType::DungeonTile69:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile69.png");
		break;
	case TileType::DungeonTile70:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile70.png");
		break;
	case TileType::DungeonTile71:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile71.png");
		break;
	case TileType::DungeonTile72:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile72.png");
		break;
	case TileType::DungeonTile73:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile73.png");
		break;
	case TileType::DungeonTile74:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile74.png");
		break;
	case TileType::DungeonTile75:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile75.png");
		break;
	case TileType::DungeonTile76:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile76.png");
		break;
	case TileType::DungeonTile77:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile77.png");
		break;
	case TileType::DungeonTile78:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile78.png");
		break;
	case TileType::DungeonTile79:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile79.png");
		break;
	case TileType::DungeonTile80:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile80.png");
		break;
	case TileType::DungeonTile81:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile81.png");
		break;
	case TileType::DungeonTile82:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile82.png");
		break;
	case TileType::DungeonTile83:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile83.png");
		break;
	case TileType::DungeonTile84:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile84.png");
		break;
	case TileType::DungeonTile85:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile85.png");
		break;
	case TileType::DungeonTile86:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile86.png");
		break;
	case TileType::DungeonTile87:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile87.png");
		break;
	case TileType::DungeonTile88:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile88.png");
		break;
	}
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}