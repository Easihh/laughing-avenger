#include "Item\HeartContainer.h"
#include "Player\Player.h"
#include "Type\Layer.h"
HeartContainer::HeartContainer(Point pos) {
	isObtained = false;
	currentFrame = 0;
	position = pos;
	texture.loadFromFile("Tileset/HeartContainer.png");
	width = Global::TileWidth;
	height = 4;
	setupMask(&fullMask, width, height, sf::Color::Magenta);
	setupMask(&mask, width, height, sf::Color::Cyan);
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void HeartContainer::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	sprite.setPosition(position.x, position.y);
	if(isCollidingWithPlayer(Worldmap) && !isObtained) {
		Player* tmp = ((Player*)findPlayer(Worldmap).get());
		if (tmp->inventory->playerBar->currentDungeon == DungeonLevel::NONE){
			position.y = tmp->position.y - Global::TileHeight;
			position.x = tmp->position.x;
			tmp->sprite.setPosition(tmp->position.x, tmp->position.y);
			tmp->isObtainingItem = true;
			Sound::playSound(GameSound::NewItem);
		}
		else currentFrame = maxFrame;
		tmp->inventory->playerBar->increaseMaxHP();
		Sound::playSound(GameSound::NewInventoryItem);
		isObtained = true;
	}
	if(isObtained){
		currentFrame++;
		if(currentFrame > maxFrame){
			Player* tmp = ((Player*)findPlayer(Worldmap).get());
			tmp->isObtainingItem = false;
			deleteNpcFromCurrentRoom(Worldmap);
			if (tmp->currentLayer == InsideShop)//secret room destroy the potion choice
				destroyPotion(Worldmap);
			destroyGameObject(Worldmap);
		}
	}
}
void HeartContainer::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(sprite);
}