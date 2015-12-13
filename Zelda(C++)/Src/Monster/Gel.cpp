#include "Monster\Gel.h"
#include "Utility\Static.h"
#include <iostream>
#include "Monster\DeathEffect.h"
#include "Misc\Tile.h"
#include "Type\RupeeType.h"
#include "Item\RupeeDrop.h"
#include "Item\ThrownBoomrang.h"
Gel::Gel(Point pos, bool canBeCollidedWith){
	position = pos;
	width = Global::TileWidth;
	height = Global::TileHeight;
	isCollideable = canBeCollidedWith;
	loadAnimation();
	isInvincible = false;
	healthPoint = 1;
	strength = 1;
	currentInvincibleFrame = 0;
	pushbackStep = 0;
	setupFullMask();
	setupMonsterMask();
	dir = Direction::None;
	getNextDirection(Direction::None);
}
void Gel::tryToChangeDirection() {
	int direction;
	int chanceToSwitch = std::rand() % 5;//20% chance to switch dir;
	if ((int)position.x %Global::TileWidth == 0 && (dir == Direction::Left || dir == Direction::Right) && chanceToSwitch == 0){
		direction = rand() % 3;
		while (direction == (int)dir){//we are changing direction,cant change to current direction.
			direction = rand() % 4;
		}
		switch (direction){
		case 0:
			dir = Direction::Right;
			break;
		case 1:
			if ((int)position.y%Global::TileHeight == 0)
				dir = Direction::Down;
			break;
		case 2:
			if ((int)position.y%Global::TileHeight == 0)
				dir = Direction::Up;
			break;
		case 3:
			dir = Direction::Left;
			break;
		}
	}
	else if ((int)position.y %Global::TileHeight == 0 && (dir == Direction::Up || dir == Direction::Down) && chanceToSwitch == 0){
		direction = rand() % 3;
		while (direction == (int)dir){//we are changing direction,cant change to current direction.
			direction = rand() % 4;
		}
		switch (direction){
		case 0:
			if ((int)position.x%Global::TileWidth == 0)
				dir = Direction::Right;
			break;
		case 1:
			dir = Direction::Down;
			break;
		case 2:
			dir = Direction::Up;
			break;
		case 3:
			if ((int)position.x%Global::TileWidth == 0)
				dir = Direction::Left;
			break;
		}
	}
}
void Gel::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(walkingAnimation->sprite);
	//mainWindow.draw(*mask);
	//mainWindow.draw(*fullMask);
}
void Gel::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	walkingAnimation->updateAnimationFrame(position);
	if (isCollidingWithBoomerang(worldMap)){
		Sound::playSound(GameSound::EnemyHit);
		ThrownBoomrang* boom = (ThrownBoomrang*)findBoomerang(worldMap).get();
		if (!boom->isReturning)
			boom->isReturning = true;
		takeDamage(1);
	}
	movement(worldMap);
	tryToChangeDirection();
	if (healthPoint <= 0){
		Point pt(position.x + (width / 4), position.y + (height / 4));
		std::shared_ptr<GameObject> add = std::make_shared<DeathEffect>(pt);
		Static::toAdd.push_back(add);
		destroyGameObject(worldMap);
		Sound::playSound(GameSound::SoundType::EnemyKill);
		std::cout << "Gel Destroyed";
	}
	updateMasks();
}

void Gel::takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction attackDir) {
	healthPoint -= damage;
}
void Gel::takeDamage(int damage){
		healthPoint -= damage;
}
int Gel::getXOffset(){
	if (dir == Direction::Left)
		return -minStep;
	else if (dir == Direction::Right)
		return minStep;
	else return 0;
}
int Gel::getYOffset(){
	if (dir == Direction::Up)
		return -minStep;
	else if (dir == Direction::Down)
		return minStep;
	else return 0;
}
void Gel::movement(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	Point offsets(getXOffset(), getYOffset());
	switch (dir){
	case Direction::Down:
	{
		Point pt(position.x, position.y + minStep);
		if (!isColliding(worldMap, fullMask, offsets) && !isOutsideRoomBound(pt))
			position.y += minStep;
		else getNextDirection(Direction::Down);
		break;
	}
	case Direction::Up:
	{
		Point pt(position.x, position.y - minStep);
		if (!isColliding(worldMap, fullMask, offsets) && !isOutsideRoomBound(pt))
			position.y -= minStep;
		else getNextDirection(Direction::Up);
		break;
	}
	case Direction::Left:
	{
		Point pt(position.x - minStep, position.y);
		if (!isColliding(worldMap, fullMask, offsets) && !isOutsideRoomBound(pt))
			position.x -= minStep;
		else getNextDirection(Direction::Left);
		break;
	}
	case Direction::Right:
	{
		Point pt(position.x + minStep, position.y);
		if (!isColliding(worldMap, fullMask, offsets) && !isOutsideRoomBound(pt))
			position.x += minStep;
		else getNextDirection(Direction::Right);
		break;
	}
	}
}
void Gel::getNextDirection(Direction blockedDir){
	const int numberofDirection = 4;
	while (dir == blockedDir || dir == Direction::None){
		int val = std::rand() % numberofDirection;
		switch (val){
		case 0:
			dir = Direction::Down;
			break;
		case 1:
			dir = Direction::Up;
			break;
		case 2:
			dir = Direction::Left;
			break;
		case 3:
			dir = Direction::Right;
			break;
		}
	}
}
void Gel::loadAnimation(){
	walkingAnimation=std::make_unique<Animation>("Gel", height, width, position, 8);
}
