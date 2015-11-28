#include "Item\DungeonMap.h"
#include "Utility\Static.h"
#include "Player\Player.h"
DungeonMap::DungeonMap(Point pos){
	position = pos;
	width = Global::TileWidth;
	height = Global::TileHeight;
	setupFullMask();
	texture.loadFromFile("Tileset/Map.png");
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void DungeonMap::draw(sf::RenderWindow& window) {
	window.draw(sprite);
}
void DungeonMap::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	if (isCollidingWithPlayer(Worldmap)){
		Player* player = (Player*)findPlayer(Worldmap).get();
		Sound::playSound(GameSound::NewInventoryItem);
		player->inventory->playerBar->hasDungeonMap = true;
		destroyGameObject(Worldmap);
	}
}