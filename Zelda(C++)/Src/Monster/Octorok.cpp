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
	loadImage("RedOctorok_Movement");
	isInvincible = false;
	healthPoint = 2;
	strength = 1;
	currentInvincibleFrame = 0;
	setupFullMask();
	mask= new sf::RectangleShape();
	mask->setFillColor(sf::Color::Transparent);
	sf::Vector2f size(16, 16);
	mask->setSize(size);
	mask->setOutlineColor(sf::Color::Blue);
	mask->setOutlineThickness(1);
	mask->setPosition(position.x + 8, position.y + 8);
	dir = Static::None;
	getNextDirection(Static::None);
}
Octorok::~Octorok(){}
void Octorok::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(anim->sprite);
	mainWindow.draw(*mask);
	mainWindow.draw(*fullMask);
}
void Octorok::update(std::vector<GameObject*>* worldMap){
	movement(worldMap);
	anim->updateAnimationFrame(dir, position);
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
bool Octorok::isColliding(std::vector<GameObject*>* worldMap){
	bool collision = false;
	Point offset(getXOffset(), getYOffset());
	for each (GameObject* obj in *worldMap)
	{
		if (dynamic_cast<Tile*>(obj))
			if (intersect(fullMask, obj->fullMask, offset)){
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
	switch (dir){
	case Static::Direction::Down:
	{
		Point pt(position.x, position.y + minStep);
		if (!isColliding(worldMap) && !isOutsideRoomBound(pt))
			position.y += minStep;
		else getNextDirection(Static::Direction::Down);
		break;
	}
	case Static::Direction::Up:
	{
		Point pt(position.x, position.y - minStep);
		if (!isColliding(worldMap) && !isOutsideRoomBound(pt))
			position.y -= minStep;
		else getNextDirection(Static::Direction::Up);
		break;
	}
	case Static::Direction::Left:
	{
		Point pt(position.x - minStep, position.y);
		if (!isColliding(worldMap) && !isOutsideRoomBound(pt))
			position.x -= minStep;
		else getNextDirection(Static::Direction::Left);
		break;
	}
	case Static::Direction::Right:
	{
		Point pt(position.x + minStep, position.y);
		if (!isColliding(worldMap) && !isOutsideRoomBound(pt))
			position.x += minStep;
		else getNextDirection(Static::Direction::Right);
		break;
	}
	}
	fullMask->setPosition(position.x, position.y);
	mask->setPosition(position.x + 8, position.y + 8);
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
void Octorok::loadImage(std::string filename){
	anim = new Animation(filename, height, width, position, 8);
}
