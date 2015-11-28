#include "Item\Compass.h"
#include "Utility\Static.h"
#include "Player\Player.h"
Compass::Compass(Point pos){
	position = pos;
	width = Global::TileWidth;
	height = Global::TileHeight;
	setupFullMask();
	texture.loadFromFile("Tileset/Compass.png");
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void Compass::draw(sf::RenderWindow& window) {
	window.draw(sprite);
}
void Compass::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	if (isCollidingWithPlayer(Worldmap)){
		Player* player = (Player*)findPlayer(Worldmap).get();
		Sound::playSound(GameSound::NewInventoryItem);
		player->inventory->playerBar->hasDungeonCompass = true;
		destroyGameObject(Worldmap);
	}
}