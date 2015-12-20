#include "Misc\Flame.h"
#include "Utility\Static.h"
Flame::Flame(Point pos, bool canBeCollidedWith){
	position = pos;
	width = Global::TileWidth;
	height = Global::TileHeight;
	setupMask(&fullMask, width, height, sf::Color::Magenta);
	flameAnimation = std::make_unique<Animation>("Fire", height, width, position, 8);
}
void Flame::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	flameAnimation.get()->updateAnimationFrame(position);
}
void Flame::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(flameAnimation.get()->sprite);
}