#include "Monster\WallMasterSpawner.h"
#include "Misc\GameObject.h"
#include "Monster\WallMaster.h"
WallMasterSpawner::WallMasterSpawner(Point pos,Direction spawnDir){
	position = pos;
	dir = spawnDir;
	depth = 2000;//higher depth than WallMaster so the WallMaster get covered by  this.
	switch (dir){
	case Direction::Up:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile34.png");
		break;
	case Direction::Right:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile87.png");
		break;
	case Direction::Down:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile2.png");
		break;
	case Direction::Left:
		texture.loadFromFile("Tileset/Dungeon/dungeonTile74.png");
		break;
	}
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void WallMasterSpawner::update(std::vector<std::shared_ptr<GameObject>>* worldMap){

}
void WallMasterSpawner::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
}