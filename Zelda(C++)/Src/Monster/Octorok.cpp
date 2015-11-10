#include "Monster\Octorok.h"
#include "Utility\Static.h"
#include <iostream>
#include "Monster\DeathEffect.h"
#include "Misc\Tile.h"
Octorok::Octorok(Point pos, bool canBeCollidedWith){
	position = pos;
	width = Global::TileWidth;
	height = Global::TileHeight;
	isCollideable = canBeCollidedWith;
	loadAnimation();
	isInvincible = false;
	healthPoint = 2;
	strength = 1;
	currentInvincibleFrame = 0;
	pushbackStep = 0;
	setupFullMask();
	mask = std::make_unique<sf::RectangleShape>();
	mask->setFillColor(sf::Color::Transparent);
	sf::Vector2f size(16, 16);
	mask->setSize(size);
	mask->setOutlineColor(sf::Color::Blue);
	mask->setOutlineThickness(1);
	mask->setPosition(position.x + 8, position.y + 8);
	dir = Direction::None;
	getNextDirection(Direction::None);
	walkAnimIndex = 0;
}
Octorok::~Octorok(){}
void Octorok::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(walkingAnimation[walkAnimIndex]->sprite);
	mainWindow.draw(*mask);
	mainWindow.draw(*fullMask);
}
void Octorok::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	if(pushbackStep == 0)
		movement(worldMap);
	else pushbackUpdate();
	for (int i = 0; i < 3;i++)
		walkingAnimation[i]->updateAnimationFrame(dir, position);
	if (healthPoint <= 0){
		Point pt(position.x + (width / 4), position.y + (height / 4));
		std::shared_ptr<GameObject> add = std::make_shared<DeathEffect>(pt);
		Static::toAdd.push_back(add);
		destroyGameObject(worldMap);
		Sound::playSound(SoundType::EnemyKill);
		std::cout << "Octorok Destroyed";
	}
	else 
	{
		checkInvincibility();
	}
	updateMasks();
}
void Octorok::takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap,Direction attackDir) {
	if (!isInvincible){
		healthPoint -= damage;
		if(healthPoint >= 1){
			Sound::playSound(EnemyHit);
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
			Sound::playSound(EnemyHit);
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
