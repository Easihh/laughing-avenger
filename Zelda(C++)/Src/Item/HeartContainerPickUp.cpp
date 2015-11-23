#include "Item\HeartContainerPickUp.h"
#include "Player\Player.h"
HeartContainerPickUp::HeartContainerPickUp(Point pos) {
	isObtained = false;
	currentFrame = 0;
	position = pos;
	texture.loadFromFile("Tileset/HeartContainer.png");
	width = Global::TileWidth;
	height = 4;
	setupFullMask();
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void HeartContainerPickUp::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	sprite.setPosition(position.x, position.y);
	if(isCollidingWithPlayer(Worldmap) && !isObtained) {
		Player* tmp = ((Player*)player.get());
		position.y = tmp->position.y - Global::TileHeight;
		position.x = tmp->position.x;
		tmp->inventory->playerBar->increaseMaxHP();
		tmp->isObtainingItem = true;
		tmp->sprite.setPosition(tmp->position.x, tmp->position.y);
		Sound::playSound(GameSound::NewItem);
		Sound::playSound(GameSound::NewInventoryItem);
		isObtained = true;
	}
	if(isObtained){
		currentFrame++;
		if(currentFrame > maxFrame){
			Player* tmp = ((Player*)player.get());
			tmp->isObtainingItem = false;
			deleteNpcFromCurrentRoom(Worldmap);
			destroyGameObject(Worldmap);
		}
	}
}
void HeartContainerPickUp::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(sprite);
}