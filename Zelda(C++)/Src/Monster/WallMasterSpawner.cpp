#include "Monster\WallMasterSpawner.h"
#include "Misc\GameObject.h"
#include "Monster\WallMaster.h"
WallMasterSpawner::WallMasterSpawner(Point pos,Direction spawnDir){
	position = pos;
	dir = spawnDir;
	/*texture.loadFromFile("Tileset/BlankTile.png");
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);*/
}
void WallMasterSpawner::update(std::vector<std::shared_ptr<GameObject>>* worldMap){

}
void WallMasterSpawner::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
}