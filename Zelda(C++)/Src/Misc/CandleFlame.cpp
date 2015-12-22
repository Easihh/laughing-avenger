#include "Misc\CandleFlame.h"
#include "Utility\Static.h"
#include "Monster\Monster.h"
#include "Misc\SecretTree.h"
#include "Monster\Aquamentus.h"
CandleFlame::CandleFlame(Point pos, Direction direction) {
	position = pos;
	flamePower = 1;
	currentDuration=currentFrame = 0;
	width = Global::TileWidth;
	height = Global::TileHeight;
	setupMask(&fullMask, width, height, sf::Color::Magenta);
	setupMask(&mask, width, height, sf::Color::Cyan);
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
void CandleFlame::checkForSecretRoom(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	for(int i = 0; i < Worldmap->size(); i++){
		if(dynamic_cast<SecretTree*>(Worldmap->at(i).get())){
			SecretTree* tmp = (SecretTree*)Worldmap->at(i).get();
			if(intersect(fullMask, tmp->fullMask) && !tmp->isActivated){
				Sound::playSound(GameSound::SecretRoom);
				tmp->texture.loadFromFile("Tileset/SecretRoom.png");
				tmp->sprite.setTexture(tmp->texture);
				tmp->isCollideable = false;
				tmp->isActivated = true;
			}
		}
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
		if (!dynamic_cast<Aquamentus*>(temp))
			temp->takeDamage(flamePower, Worldmap, dir);
	}
	fullMask->setPosition(position.x, position.y);
	if(currentDuration > maxDuration){
		checkForSecretRoom(Worldmap);
		destroyGameObject(Worldmap);
	}
}
void CandleFlame::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(flameAnimation.get()->sprite);
	//mainWindow.draw(*fullMask);
}