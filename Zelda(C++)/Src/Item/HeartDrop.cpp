#include "Item\HeartDrop.h"
#include "Utility\Static.h"
#include "Player\Player.h"
HeartDrop::HeartDrop(Point pos) {
	position = pos;
	width = Global::TileWidth;
	height = Global::TileHeight;
	setImage();
	setupMask(&fullMask, width, height, sf::Color::Magenta);
	setupMask(&mask, width, height, sf::Color::Cyan);
}
void HeartDrop::setImage() {
	anim = std::make_unique<Animation>("heart", height, width, position, 8);
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void HeartDrop::draw(sf::RenderWindow& window) {
	window.draw(anim->sprite);
}
void HeartDrop::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	anim->updateAnimationFrame(position);
	if (isCollidingWithPlayer(Worldmap) || isCollidingWithBoomerang(Worldmap)){
		Player* temp = ((Player*)findPlayer(Worldmap).get());
		Sound::playSound(GameSound::GetHeart);
		temp->inventory->playerBar->increaseCurrentHP();
		destroyGameObject(Worldmap);
	}
}