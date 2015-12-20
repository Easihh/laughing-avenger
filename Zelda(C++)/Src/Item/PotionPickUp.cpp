#include "Item\PotionPickUp.h"
#include "Player\Player.h"
#include "Item\Potion.h"
PotionPickUp::PotionPickUp(Point pos) {
	isObtained = false;
	currentFrame = 0;
	position = pos;
	texture.loadFromFile("Tileset/RedPotion.png");
	width = Global::TileWidth;
	height = 4;
	setupMask(&fullMask, width, height, sf::Color::Magenta);
	setupMask(&mask, width, height, sf::Color::Cyan);
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void PotionPickUp::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	sprite.setPosition(position.x, position.y);
	if (isCollidingWithPlayer(Worldmap) && !isObtained) {
		Player* tmp = ((Player*)findPlayer(Worldmap).get());
		position.y = tmp->position.y - Global::TileHeight;
		position.x = tmp->position.x;
		tmp->isObtainingItem = true;
		tmp->sprite.setPosition(tmp->position.x, tmp->position.y);
		Sound::playSound(GameSound::NewItem);
		Sound::playSound(GameSound::NewInventoryItem);
		isObtained = true;
		Potion* potion = ((Potion*)tmp->inventory->items.at(5).get());
		potion->isActive = true;
	}
	if (isObtained){
		currentFrame++;
		if (currentFrame > maxFrame){
			Player* tmp = ((Player*)findPlayer(Worldmap).get());
			tmp->isObtainingItem = false;
			deleteNpcFromCurrentRoom(Worldmap);
			if (tmp->currentLayer == InsideShop)//secret room destroy the HeartContainer choice
				destroyHeartContainer(Worldmap);
			destroyGameObject(Worldmap);
		}
	}
}
void PotionPickUp::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(sprite);
}