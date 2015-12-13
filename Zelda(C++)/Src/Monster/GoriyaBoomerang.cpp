#include "Monster\GoriyaBoomerang.h"
#include "Player\Player.h"
GoriyaBoomerang::GoriyaBoomerang(Point pos, Direction direction) {
	depth = 40;
	currentDistance = 0;
	position = pos;
	strength = 1;
	isReturning = false;
	boomrangDir = direction;
	width = Global::TileWidth;
	height = Global::TileHeight;
	setupFullMask();
	setupMonsterMask();
	setupInitialPosition();
	returnDirectionIsSet = false;
	hasBeenBlocked = false;
	boomrangAnimation = std::make_unique<Animation>("Boomerang", height, width, position, 8);
}
void GoriyaBoomerang::setupInitialPosition() {
	switch (boomrangDir){
	case Direction::Right:
		position.x += Global::TileWidth;
		break;
	case Direction::Left:
		position.x -= Global::TileWidth;
		break;
	case Direction::Down:
		position.y += Global::TileHeight;
		break;
	case Direction::Up:
		position.y -= Global::TileHeight;
		break;
	}
	sprite.setPosition(position.x, position.y);
	fullMask->setPosition(position.x, position.y);
	mask->setPosition(position.x, position.y);	
}
void GoriyaBoomerang::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(boomrangAnimation->sprite);
	//mainWindow.draw(*fullMask);
}
void GoriyaBoomerang::setReturnDirection(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	switch (boomrangDir){
	case Direction::Left:
		boomrangDir = Direction::Right;
		break;
	case Direction::Right:
		boomrangDir = Direction::Left;
		break;
	case Direction::Up:
		boomrangDir = Direction::Down;
		break;
	case Direction::Down:
		boomrangDir = Direction::Up;
		break;
	}
	returnDirectionIsSet = true;
}
void GoriyaBoomerang::boomrangMovement() {
	switch (boomrangDir){
	case Direction::Right:
		position.x += boomrangSpeed;
		break;
	case Direction::Left:
		position.x -= boomrangSpeed;
		break;
	case Direction::Down:
		position.y += boomrangSpeed;
		break;
	case Direction::Up:
		position.y -= boomrangSpeed;
		break;
	}
	currentDistance += boomrangSpeed;
	sprite.setPosition(position.x, position.y);
	if (isOutsideRoomBound(position) || currentDistance > maxDistance){
		isReturning = true;
	}
	fullMask->setPosition(position.x, position.y);
}
void GoriyaBoomerang::checkIfPlayerCanBlock(std::vector<std::shared_ptr<GameObject>>* worldMap){
	Player* temp = (Player*)findPlayer(worldMap).get();
	switch (boomrangDir){
	case Direction::Down:
		if (temp->dir == Direction::Up){
			Sound::playSound(GameSound::ShieldBlock);
			hasBeenBlocked = true;
		}
		else isReturning = true;
		break;
	case Direction::Up:
		if (temp->dir == Direction::Down){
			Sound::playSound(GameSound::ShieldBlock);
			hasBeenBlocked = true;
		}
		else isReturning = true;
		break;
	case Direction::Left:
		if (temp->dir == Direction::Right){
			Sound::playSound(GameSound::ShieldBlock);
			hasBeenBlocked = true;
		}
		else isReturning = true;
		break;
	case Direction::Right:
		if (temp->dir == Direction::Left){
			Sound::playSound(GameSound::ShieldBlock);
			hasBeenBlocked = true;
		}
		else isReturning = true;
		break;
	}
}
void GoriyaBoomerang::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	boomrangMovement();
	boomrangAnimation->updateAnimationFrame(position);
	if (isReturning && !returnDirectionIsSet){
		setReturnDirection(worldMap);
	}
	if (isCollidingWithPlayer(worldMap))
		checkIfPlayerCanBlock(worldMap);
}