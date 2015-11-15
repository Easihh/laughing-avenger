#include "Misc\CandleFlame.h"
#include "Utility\Static.h"
#include "Monster\Monster.h"
CandleFlame::CandleFlame(Point pos, Direction direction) {
	position = pos;
	flamePower = 1;
	currentDuration=currentFrame = 0;
	width = Global::TileWidth;
	height = Global::TileHeight;
	setupFullMask();
	flameAnimation = std::make_unique<Animation>("Fire", height, width, position, 8);
	dir = direction;
	setupFlame();
}
void CandleFlame::setupFlame() {
	switch(dir){
	case Direction::Right:
		position.x += width;
		break;
	case Direction::Left:
		position.x -= width;
		break;
	case Direction::Up:
		position.y -= width;
		break;
	case Direction::Down:
		position.y += width;
		break;
	}
}
void CandleFlame::updateFlameMovement() {
	int stepPerUpdate = 1;
	switch(dir){
	case Direction::Right:
	position.x += stepPerUpdate;
	break;
	case Direction::Left:
	position.x -= stepPerUpdate;
	break;
	case Direction::Up:
	position.y -= stepPerUpdate;
	break;
	case Direction::Down:
	position.y += stepPerUpdate;
	break;
	}
}
void CandleFlame::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	flameAnimation.get()->updateAnimationFrame(position);
	currentDuration++;
	if(currentFrame < maxFrame){
		currentFrame++;
		updateFlameMovement();
	}
	if(isCollidingWithMonster(Worldmap)){
		Monster* temp = ((Monster*)collidingMonster.get());
		temp->takeDamage(flamePower, Worldmap, dir);
	}
	fullMask->setPosition(position.x, position.y);
	if(currentDuration>maxDuration)
		destroyGameObject(Worldmap);
}
void CandleFlame::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(flameAnimation.get()->sprite);
	mainWindow.draw(*fullMask);
}