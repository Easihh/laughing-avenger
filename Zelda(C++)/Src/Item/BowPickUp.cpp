#include "Item\BowPickUp.h"
#include "Player\Player.h"
BowPickUp::BowPickUp(Point pos) {
	isObtained = false;
	currentFrame = 0;
	position = pos;
	texture.loadFromFile("Tileset/Bow.png");
	width = Global::TileWidth;
	height = 4;
	setupFullMask();
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void BowPickUp::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	sprite.setPosition(position.x, position.y);
	if (isCollidingWithPlayer(Worldmap) && !isObtained) {
		Player* tmp = ((Player*)findPlayer(Worldmap).get());
		position.y = tmp->position.y - Global::TileHeight;
		position.x = tmp->position.x;
		tmp->inventory->acquireBow();
		tmp->isObtainingItem = true;
		tmp->sprite.setPosition(tmp->position.x, tmp->position.y);
		Sound::playSound(GameSound::NewItem);
		Sound::playSound(GameSound::NewInventoryItem);
		isObtained = true;
	}
	if (isObtained){
		currentFrame++;
		if (currentFrame > maxFrame){
			Player* tmp = ((Player*)findPlayer(Worldmap).get());
			tmp->isObtainingItem = false;
			destroyGameObject(Worldmap);
		}
	}
}
void BowPickUp::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(sprite);
}