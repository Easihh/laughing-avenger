#include "Misc\ShopKey.h"
#include "Utility\Static.h"
#include "Player\Player.h"
ShopKey::ShopKey(Point pos){
	itemPrice = 100;
	position = pos;
	origin = pos;
	currentFrame = 0;
	width = Global::TileWidth;
	height = 4;
	setupFullMask();
	isVisible = true;
	isObtained = false;
	texture.loadFromFile("Tileset/Key.png");
	font.loadFromFile("zelda.ttf");
	txt.setFont(font);
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void ShopKey::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	sprite.setPosition(position.x, position.y);
	if (isCollidingWithPlayer(Worldmap) && !isObtained && isVisible) {
		Player* tmp = ((Player*)findPlayer(Worldmap).get());
		if (tmp->inventory->playerBar->diamondAmount >= itemPrice){
			position.y = tmp->position.y - Global::TileHeight;
			position.x = tmp->position.x;
			tmp->isObtainingItem = true;
			tmp->sprite.setPosition(tmp->position.x, tmp->position.y);
			Sound::playSound(GameSound::NewItem);
			Sound::playSound(GameSound::NewInventoryItem);
			tmp->inventory->playerBar->increaseKeyAmount();
			tmp->inventory->playerBar->diamondAmount -= itemPrice;
			isObtained = true;
			hideOtherShopItems(Worldmap);
		}
	}
	if (isObtained){
		currentFrame++;
		if (currentFrame > maxFrame){
			currentFrame = 0;
			Player* tmp = ((Player*)findPlayer(Worldmap).get());
			tmp->isObtainingItem = false;
			resetShopItem();
		}
	}
}
void ShopKey::draw(sf::RenderWindow& mainWindow) {
	if (isVisible){
		mainWindow.draw(sprite);
	}
	if (!isObtained && isVisible)
		drawCost(mainWindow, itemPrice);
}