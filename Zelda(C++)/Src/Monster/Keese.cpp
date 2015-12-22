#include "Monster\Keese.h"
#include "Utility\Static.h"
#include <iostream>
#include "Monster\DeathEffect.h"
#include "Misc\Tile.h"
#include "Type\RupeeType.h"
#include "Item\RupeeDrop.h"
#include "Item\ThrownBoomrang.h"
#include "Player\Player.h"
Keese::Keese(Point pos, bool canBeCollidedWith) {
	depth = 5000;
	position = pos;
	width = Global::TileWidth;
	height = Global::TileHeight;
	isCollideable = canBeCollidedWith;
	loadAnimation();
	isInvincible = false;
	healthPoint = 1;
	strength = 1;
	currentInvincibleFrame = 0;
	setupMask(&fullMask, width, height, sf::Color::Magenta);
	setupMask(&mask, width, height, sf::Color::Cyan);
	dir = Direction::None;
	getNextDirection(Direction::None);
	timeSinceLastTryDirectionChange = 0;
}
void Keese::processDeath(std::vector<std::shared_ptr<GameObject>>* worldMap){
	Point pt(position.x + (width / 4), position.y + (height / 4));
	std::shared_ptr<GameObject> add = std::make_shared<DeathEffect>(pt);
	Static::toAdd.push_back(add);
	destroyGameObject(worldMap);
	Sound::playSound(GameSound::SoundType::EnemyKill);
	std::cout << "Keese Destroyed";
}
void Keese::tryToChangeDirection() {
	int direction;
	timeSinceLastTryDirectionChange++;
	int chanceToSwitch = std::rand() % 5;//20% chance to switch dir;
	direction = rand() % 8;
	if (chanceToSwitch == 0 && timeSinceLastTryDirectionChange >= minimumTimeSinceLastDirectionChange){
		timeSinceLastTryDirectionChange = 0;
		while (direction == (int)dir){//we are changing direction,cant change to current direction.
			direction = rand() % 8;
		}
		switch (direction){
		case 0:
			dir = Direction::Right;
			break;
		case 1:
			dir = Direction::Down;
			break;
		case 2:
			dir = Direction::Up;
			break;
		case 3:
			dir = Direction::Left;
			break;
		case 4:
			dir = Direction::TopLeft;
			break;
		case 5:
			dir = Direction::TopRight;
			break;
		case 6:
			dir = Direction::BottomLeft;
			break;
		case 7:
			dir = Direction::BottomRight;
			break;
		}
	}
}
void Keese::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(keeseAnimation->sprite);
	//mainWindow.draw(*mask);
	//mainWindow.draw(*fullMask);
}
void Keese::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	if (isCollidingWithPlayer(worldMap)){
		Player* temp = (Player*)findPlayer(worldMap).get();
		temp->takeDamage(worldMap, this);
	}
	keeseAnimation->updateAnimationFrame(position);
	movement(worldMap);
	tryToChangeDirection();
	if (isCollidingWithBoomerang(worldMap)){
		takeDamage(1);
		ThrownBoomrang* boom = (ThrownBoomrang*)findBoomerang(worldMap).get();
		if (!boom->isReturning)
			boom->isReturning=true;
	}
	if (healthPoint <= 0)
		processDeath(worldMap);
	updateMasks();
}
void Keese::dropItemOnDeath() {}
void Keese::takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction attackDir) {
		healthPoint -= damage;
}
void Keese::takeDamage(int damage) {
		healthPoint -= damage;
}
int Keese::getXOffset() {
	if(dir == Direction::Left)
		return -minStep;
	else if(dir == Direction::Right)
		return minStep;
	else return 0;
}
int Keese::getYOffset() {
	if(dir == Direction::Up)
		return -minStep;
	else if(dir == Direction::Down)
		return minStep;
	else return 0;
}
void Keese::movement(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	Point offsets(getXOffset(), getYOffset());
	switch(dir){
	case Direction::Down:
	{
		Point pt(position.x, position.y + minStep);
		if(!isOutsideRoomBound(pt))
			position.y += minStep;
		else getNextDirection(Direction::Down);
		break;
	}
	case Direction::Up:
	{
		Point pt(position.x, position.y - minStep);
		if(!isOutsideRoomBound(pt))
			position.y -= minStep;
		else getNextDirection(Direction::Up);
		break;
	}
	case Direction::Left:
	{
		Point pt(position.x - minStep, position.y);
		if(!isOutsideRoomBound(pt))
			position.x -= minStep;
		else getNextDirection(Direction::Left);
		break;
	}
	case Direction::Right:
	{
		Point pt(position.x + minStep, position.y);
		if(!isOutsideRoomBound(pt))
			position.x += minStep;
		else getNextDirection(Direction::Right);
		break;
	}
	case Direction::TopRight:
	{
		Point pt(position.x + minStep, position.y-minStep);
		if (!isOutsideRoomBound(pt)){
			position.x += minStep;
			position.y -= minStep;
		}
		else getNextDirection(Direction::TopRight);
		break;
	}
	case Direction::TopLeft:
	{
		Point pt(position.x - minStep, position.y - minStep);
		if (!isOutsideRoomBound(pt)){
			position.x -= minStep;
			position.y -= minStep;
		}
		else getNextDirection(Direction::TopLeft);
		break;
	}
	case Direction::BottomLeft:
	{
		Point pt(position.x - minStep, position.y + minStep);
		if (!isOutsideRoomBound(pt)){
			position.x -= minStep;
			position.y += minStep;
		}
		else getNextDirection(Direction::BottomLeft);
		break;
	}
	case Direction::BottomRight:
	{
		Point pt(position.x + minStep, position.y + minStep);
		if (!isOutsideRoomBound(pt)){
			position.x += minStep;
			position.y += minStep;
		}
		else getNextDirection(Direction::BottomRight);
		break;
	}
	}
}
void Keese::getNextDirection(Direction blockedDir) {
	const int numberofDirection = 8;
	while(dir == blockedDir || dir == Direction::None){
		int val = std::rand() % numberofDirection;
		switch(val){
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
		case 4:
		dir = Direction::TopLeft;
		break;
		case 5:
		dir = Direction::TopRight;
		break;
		case 6:
		dir = Direction::BottomRight;
		break;
		case 7:
		dir = Direction::BottomLeft;
		break;
		}
	}
}
void Keese::loadAnimation() {
	keeseAnimation =std::make_unique<Animation>("Keese", height, width, position, 15);
}
