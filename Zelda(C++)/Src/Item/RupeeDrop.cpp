#include "Item\RupeeDrop.h"
#include "Utility\Static.h"
#include "Player\Player.h"
RupeeDrop::RupeeDrop(Point pos, RupeeType type) {
	position = pos;
	rtype = type;
	width = Global::TileWidth;
	height = Global::TileHeight;
	setImage();
	setupFullMask();
}
void RupeeDrop::setImage() {
	switch(rtype){
	case RupeeType::OrangeRupee:
		texture.loadFromFile("Tileset/orangeRupee.png");
		rupeeValue = 1;
		break;
		case RupeeType::BlueRupee:
		texture.loadFromFile("Tileset/blueRupee.png");
		rupeeValue = 5;
		break;
	}
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void RupeeDrop::draw(sf::RenderWindow& window) {
	window.draw(sprite);
}
void RupeeDrop::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	if(isCollidingWithPlayer(Worldmap) || isCollidingWithBoomerang(Worldmap)){
		Player* temp = ((Player*)findPlayer(Worldmap).get());
		Sound::playSound(GameSound::Selector);
		temp->inventory->playerBar->increaseRupeeAmount(rupeeValue);
		destroyGameObject(Worldmap);
	}
}