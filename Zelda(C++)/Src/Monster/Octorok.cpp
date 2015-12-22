#include "Monster\Octorok.h"
#include "Utility\Static.h"
#include <iostream>
#include "Monster\DeathEffect.h"
#include "Misc\Tile.h"
#include "Type\RupeeType.h"
#include "Item\RupeeDrop.h"
#include "Item\ThrownBoomrang.h"
#include "Item\HeartDrop.h"
#include "Player\Player.h"
Octorok::Octorok(Point pos, bool canBeCollidedWith){
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
	setupMask(&fullMask, width, height, sf::Color::Magenta);
	setupMask(&mask, width, height, sf::Color::Cyan);
	dir = Direction::None;
	isParalyzed = false;
	currentParalyzeTime = 0;
	getNextDirection(Direction::None);
	walkAnimIndex = 0;
}
void Octorok::tryToChangeDirection() {
	int direction;
	int chanceToSwitch = std::rand() % 5;//20% chance to switch dir;
 	if((int)position.x %Global::TileWidth == 0 &&(dir==Direction::Left || dir==Direction::Right) && chanceToSwitch==0){
		direction = rand() % 3;
		while(direction == (int)dir){//we are changing direction,cant change to current direction.
			direction = rand() % 4;
		}
		switch(direction){
		case 0:
			dir = Direction::Right;
			break;
		case 1:
			if((int)position.y%Global::TileHeight==0)
				dir = Direction::Down;
			break;
		case 2:
			if((int)position.y%Global::TileHeight == 0)
				dir = Direction::Up;
			break;
		case 3:
			dir = Direction::Left;
			break;
		}
	}
	else if((int)position.y %Global::TileHeight == 0 && (dir == Direction::Up || dir == Direction::Down) && chanceToSwitch == 0){
		direction = rand() % 3;
		while(direction == (int)dir){//we are changing direction,cant change to current direction.
			direction = rand() % 4;
		}
		switch(direction){
		case 0:
			if((int)position.x%Global::TileWidth == 0)
				dir = Direction::Right;
			break;
		case 1:
			dir = Direction::Down;
			break;
		case 2:
			dir = Direction::Up;
			break;
		case 3:
			if((int)position.x%Global::TileWidth == 0)
				dir = Direction::Left;
			break;
		}
	}
}
void Octorok::processDeath(std::vector<std::shared_ptr<GameObject>>* worldMap){
	Point pt(position.x + (width / 4), position.y + (height / 4));
	std::shared_ptr<GameObject> add = std::make_shared<DeathEffect>(pt);
	Static::toAdd.push_back(add);
	destroyGameObject(worldMap);
	Sound::playSound(GameSound::SoundType::EnemyKill);
	dropItemOnDeath();
	std::cout << "Octorok Destroyed";
}
void Octorok::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(walkingAnimation[walkAnimIndex]->sprite);
	//mainWindow.draw(*mask);
	//mainWindow.draw(*fullMask);
}
void Octorok::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	if (isCollidingWithPlayer(worldMap)){
		Player* temp = (Player*)findPlayer(worldMap).get();
		temp->takeDamage(worldMap,this);
	}
	checkParalyzeStatus();
	if (isCollidingWithBoomerang(worldMap)){
		Sound::playSound(GameSound::EnemyHit); 
		isParalyzed = true;
		ThrownBoomrang* boom = (ThrownBoomrang*)findBoomerang(worldMap).get();
		if (!boom->isReturning)
			boom->isReturning=true;
	}
	if(pushbackStep == 0 && !isParalyzed){
		movement(worldMap);
		tryToChangeDirection();
	}
	else if(pushbackStep!=0 && !isParalyzed)
		pushbackUpdate();
	for (int i = 0; i < 3;i++)
		walkingAnimation[i]->updateAnimationFrame(dir, position);
	if (healthPoint <= 0)
		processDeath(worldMap);
	else checkInvincibility();
	updateMasks();
}
void Octorok::dropItemOnDeath() {
	bool willDropItem = false;
	int value  = std::rand() % 10;
	if(value < 3) //30% chance to drop item
		willDropItem = true;
	if(willDropItem){
		int itemDropId = std::rand() % 3;
		std::shared_ptr<GameObject> itemDropped;
		switch(itemDropId){
		case 0:
			itemDropped = std::make_shared<RupeeDrop>(position, RupeeType::OrangeRupee);
			break;
		case 1:
			itemDropped = std::make_shared<RupeeDrop>(position, RupeeType::BlueRupee);
			break;
		case 2:
			itemDropped = std::make_shared<HeartDrop>(position);
			break;
		}
		Static::toAdd.push_back(itemDropped);
	}
}
void Octorok::takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap,Direction attackDir) {
	if (!isInvincible){
		healthPoint -= damage;
		if(healthPoint >= 1){
			Sound::playSound(GameSound::EnemyHit);
			if(pushbackStep==0)
				pushBack(worldMap, attackDir);
		}
		isInvincible = true;
		walkAnimIndex = 1;
	}
}
void Octorok::takeDamage(int damage){
	if (!isInvincible){
		if(healthPoint > damage)
			Sound::playSound(GameSound::EnemyHit);
		healthPoint -= damage;
		isInvincible = true;
		walkAnimIndex = 1;
	}
}
int Octorok::getXOffset(){
	if (dir == Direction::Left)
		return -minStep;
	else if (dir == Direction::Right)
		return minStep;
	else return 0;
}
int Octorok::getYOffset(){
	if (dir == Direction::Up)
		return -minStep;
	else if (dir == Direction::Down)
		return minStep;
	else return 0;
}
void Octorok::movement(std::vector<std::shared_ptr<GameObject>>* worldMap) {
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
		if (!isColliding(worldMap, fullMask,offsets) && !isOutsideRoomBound(pt))
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
void Octorok::getNextDirection(Direction blockedDir){
	const int numberofDirection = 4;
	while(dir == blockedDir || dir == Direction::None){
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
void Octorok::loadAnimation(){
	walkingAnimation.push_back(std::make_unique<Animation>("RedOctorok_Movement", height, width, position, 8));
	walkingAnimation.push_back(std::make_unique<Animation>("RedOctorok_Hit1", height, width, position, 8));
	walkingAnimation.push_back(std::make_unique<Animation>("RedOctorok_Hit2", height, width, position, 8));
}
