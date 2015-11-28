#include "Item\Triforce.h"
#include "Player\Player.h"
Triforce::Triforce(Point pos) {
	isObtained = false;
	currentFrame = 0;
	position = pos;
	texture.loadFromFile("Tileset/Triforce.png");
	width = Global::TileWidth;
	height = 4;
	setupFullMask();
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void Triforce::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	sprite.setPosition(position.x, position.y);
	if (isCollidingWithPlayer(Worldmap) && !isObtained) {
		Player* tmp = ((Player*)findPlayer(Worldmap).get());
		position.y = tmp->position.y - Global::TileHeight;
		position.x = tmp->position.x;
		tmp->isObtainingItem = true;
		tmp->sprite.setPosition(tmp->position.x, tmp->position.y);
		Sound::playSound(GameSound::NewInventoryItem);
		Sound::playSound(GameSound::Triforce);
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
void Triforce::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(sprite);
}