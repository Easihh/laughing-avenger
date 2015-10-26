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
	healthPoint = 20;
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
	dir = Static::None;
	getNextDirection(Static::None);
	walkAnimIndex = 0;
}
Octorok::~Octorok(){}
void Octorok::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(walkingAnimation[walkAnimIndex]->sprite);
	mainWindow.draw(*mask);
	mainWindow.draw(*fullMask);
}
void Octorok::update(std::vector<GameObject*>* worldMap){
	if (pushbackStep == 0)
		movement(worldMap);
	else pushbackUpdate();
	for (int i = 0; i < 3;i++)
		walkingAnimation[i]->updateAnimationFrame(dir, position);
	if (healthPoint <= 0){
		Point pt(position.x + (width / 4), position.y + (height / 4));
		Static::toAdd.push_back(new DeathEffect(pt));
		Static::toDelete.push_back(this);
		std::cout << "Octorok Destroyed";
	}
	else 
	{
		checkInvincibility();
	}
}
void Octorok::pushbackUpdate(){
	//positive pushback mean same direction as currently facing.
	int step = stepPerPushBackUpdate;
	if (std::abs(pushbackStep) < stepPerPushBackUpdate)
		step = std::abs(pushbackStep);
	switch (dir){
	case Static::Direction::Down:
		if (pushbackStep < 0)
			position.y -= step;
		else position.y += step;
		break;
	case Static::Direction::Up:
		if (pushbackStep < 0)
			position.y += step;
		else position.y -= step;
		break;
	case Static::Direction::Left:
		if (pushbackStep < 0)
			position.x += step;
		else position.x -= step;
		break;
	case Static::Direction::Right:
		if (pushbackStep < 0)
			position.x -= step;
		else position.x += step;
		break;
	}
	if (pushbackStep>0)
		pushbackStep -= step;
	else pushbackStep += step;
	updateMasks();
}
void Octorok::takeDamage(int damage, std::vector<GameObject*>* worldMap, Static::Direction swordDir){
	if (!isInvincible){
		healthPoint -= damage;
		if (healthPoint >= 1)
			pushBack(worldMap,swordDir);
		isInvincible = true;
		walkAnimIndex = 1;
	}
}
void Octorok::pushBack(std::vector<GameObject*>* worldMap, Static::Direction swordDir){
	float pushBackMinDistance = 0;
	sf::Vector2f size(width, height);
	std::unique_ptr<sf::RectangleShape> pushbackLineCheck = std::make_unique<sf::RectangleShape>();
	pushbackLineCheck->setPosition(position.x, position.y);
	pushbackLineCheck->setSize(size);
	switch (swordDir){
	case Static::Direction::Up:{
		for (pushBackMinDistance = 0; pushBackMinDistance < pushBackMaxDistance; pushBackMinDistance++){
			Point pt(0, -pushBackMinDistance);
			if (isColliding(worldMap, pushbackLineCheck, pt))
				break;
		}
		if (!isOutsideRoomBound(Point(position.x, position.y - pushBackMinDistance))){
				if (dir==Static::Up)
					pushbackStep += pushBackMinDistance;
				else if (dir==Static::Down)
					pushbackStep -= pushBackMinDistance;
			}
		break;
	}
	case Static::Direction::Down:{
		for (pushBackMinDistance = 0; pushBackMinDistance < pushBackMaxDistance; pushBackMinDistance++){
			Point pt(0, pushBackMinDistance);
			if (isColliding(worldMap, pushbackLineCheck, pt))
				break;
		}
		if (!isOutsideRoomBound(Point(position.x, position.y + pushBackMinDistance))){
			if (dir == Static::Up)
				pushbackStep -= pushBackMinDistance;
			else if (dir == Static::Down)
				pushbackStep += pushBackMinDistance;
		}
	break;
}
	case Static::Direction::Right:{
		for (pushBackMinDistance = 0; pushBackMinDistance < pushBackMaxDistance; pushBackMinDistance++){
			Point pt(pushBackMinDistance, 0);
			if (isColliding(worldMap, pushbackLineCheck, pt))
				break;
		}
		if (!isOutsideRoomBound(Point(position.x + pushBackMinDistance, position.y))){
				if (dir == Static::Left)
					pushbackStep -= pushBackMinDistance;
				else if (dir == Static::Right)
					pushbackStep += pushBackMinDistance;
			}
		break;
	}
	case Static::Direction::Left:{
		for (pushBackMinDistance = 0; pushBackMinDistance < pushBackMaxDistance; pushBackMinDistance++){
			Point pt(-pushBackMinDistance, 0);
			if (isColliding(worldMap, pushbackLineCheck, pt))
				break;
		}
			if (!isOutsideRoomBound(Point(position.x - pushBackMinDistance, position.y))){
				if (dir == Static::Left)
					pushbackStep += pushBackMinDistance;
				else if (dir == Static::Right)
					pushbackStep -= pushBackMinDistance;
			}
		break;
	}
	}
}
bool Octorok::isColliding(std::vector<GameObject*>* worldMap, std::unique_ptr<sf::RectangleShape>& mask, Point offsets){
	bool collision = false;
	for each (GameObject* obj in *worldMap)
	{
		if (dynamic_cast<Tile*>(obj))
			if (intersect(fullMask, obj->fullMask, offsets)){
				collision = true;
				std::cout << "CollisionX:" << obj->position.x << std::endl;
				std::cout << "CollisionY:" << obj->position.y << std::endl;
				std::cout << "X:" << position.x << std::endl;
				std::cout << "Y:" << position.y << std::endl;
			}
	}
	return collision;
}
bool Octorok::isOutsideRoomBound(Point pos){
	int worldX = (position.y-Global::inventoryHeight)/Global::roomHeight;
	int worldY = position.x/Global::roomWidth;
	bool outsideBoundary = false;
	if (pos.x+width > (Global::roomWidth*worldY) + Global::roomWidth)
		outsideBoundary = true;
	else if (pos.x-width < (Global::roomWidth*worldY))
		outsideBoundary = true;
	else if (pos.y-height < (Global::roomHeight*worldX) + Global::inventoryHeight)
		outsideBoundary = true;
	else if (pos.y+height >(Global::roomHeight*worldX) + Global::roomHeight + Global::inventoryHeight)
		outsideBoundary = true;
	return outsideBoundary;
}
int Octorok::getXOffset(){
	if (dir == Static::Direction::Left)
		return -minStep;
	else if (dir == Static::Direction::Right)
		return minStep;
	else return 0;
}
int Octorok::getYOffset(){
	if (dir == Static::Direction::Up)
		return -minStep;
	else if (dir == Static::Direction::Down)
		return minStep;
	else return 0;
}
void Octorok::movement(std::vector<GameObject*>* worldMap){
	Point offsets(getXOffset(), getYOffset());
	switch (dir){
	case Static::Direction::Down:
	{
		Point pt(position.x, position.y + minStep);
		if (!isColliding(worldMap, fullMask, offsets) && !isOutsideRoomBound(pt))
			position.y += minStep;
		else getNextDirection(Static::Direction::Down);
		break;
	}
	case Static::Direction::Up:
	{
		Point pt(position.x, position.y - minStep);
		if (!isColliding(worldMap, fullMask,offsets) && !isOutsideRoomBound(pt))
			position.y -= minStep;
		else getNextDirection(Static::Direction::Up);
		break;
	}
	case Static::Direction::Left:
	{
		Point pt(position.x - minStep, position.y);
		if (!isColliding(worldMap, fullMask, offsets) && !isOutsideRoomBound(pt))
			position.x -= minStep;
		else getNextDirection(Static::Direction::Left);
		break;
	}
	case Static::Direction::Right:
	{
		Point pt(position.x + minStep, position.y);
		if (!isColliding(worldMap, fullMask, offsets) && !isOutsideRoomBound(pt))
			position.x += minStep;
		else getNextDirection(Static::Direction::Right);
		break;
	}
	}
	updateMasks();
}
void Octorok::getNextDirection(Static::Direction blockedDir){
	const int numberofDirection = 4;
	while (dir == blockedDir || dir == Static::None){
		int val = std::rand() % numberofDirection;
		switch (val){
		case 0:
			dir = Static::Down;
			break;
		case 1:
			dir = Static::Up;
			break;
		case 2:
			dir = Static::Left;
			break;
		case 3:
			dir = Static::Right;
			break;
		}
	}
}
void Octorok::loadAnimation(){
	walkingAnimation[0] = new Animation("RedOctorok_Movement", height, width, position, 8);
	walkingAnimation[1] = new Animation("RedOctorok_Hit1", height, width, position, 8);
	walkingAnimation[2] = new Animation("RedOctorok_Hit2", height, width, position, 8);
}
