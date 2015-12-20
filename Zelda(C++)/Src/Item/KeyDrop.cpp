#include "Item\KeyDrop.h"
#include "Utility\Static.h"
#include "Player\Player.h"
KeyDrop::KeyDrop(Point pos) {
	position = pos;
	width = Global::TileWidth;
	height = Global::TileHeight;
	setImage();
	setupMask(&fullMask, width, height, sf::Color::Magenta);
	setupMask(&mask, width, height, sf::Color::Cyan);
}
void KeyDrop::setImage() {
	texture.loadFromFile("Tileset/Key.png");
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void KeyDrop::draw(sf::RenderWindow& window) {
	window.draw(sprite);
}
void KeyDrop::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	if (isCollidingWithPlayer(Worldmap)){
		Player* temp = ((Player*)findPlayer(Worldmap).get());
		Sound::playSound(GameSound::GetHeart);
		temp->inventory->playerBar->increaseKeyAmount();
		destroyGameObject(Worldmap);
	}
}