#include "Item\RupeeDrop.h"
#include "Utility\Static.h"
#include "Player\Player.h"
RupeeDrop::RupeeDrop(Point pos, RupeeType type) {
	position = pos;
	rtype = type;
	width = Global::TileWidth;
	height = Global::TileHeight;
	setImage();
	setupMask(&fullMask, width, height, sf::Color::Magenta);
	setupMask(&mask, width, height, sf::Color::Cyan);
}
void RupeeDrop::setImage() {
	switch(rtype){
	case RupeeType::OrangeRupee:
		anim = std::make_unique<Animation>("rupee", height, width, position, 8);
		rupeeValue = 1;
		break;
		case RupeeType::BlueRupee:
		anim = std::make_unique<Animation>("blueRupee", height, width, position, 8);
		rupeeValue = 5;
		break;
	}
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void RupeeDrop::draw(sf::RenderWindow& window) {
	window.draw(anim->sprite);
}
void RupeeDrop::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	anim->updateAnimationFrame(position);
	if(isCollidingWithPlayer(Worldmap) || isCollidingWithBoomerang(Worldmap)){
		Player* temp = ((Player*)findPlayer(Worldmap).get());
		Sound::playSound(GameSound::Selector);
		temp->inventory->playerBar->increaseRupeeAmount(rupeeValue);
		destroyGameObject(Worldmap);
	}
}