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
void Octorok::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	if (pushbackStep == 0)
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
void Octorok::takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap, Static::Direction attackDir) {
	if (!isInvincible){
		healthPoint -= damage;
		if(healthPoint >= 1){
			Sound::playSound(EnemyHit);
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
void Octorok::pushBack(std::vector<std::shared_ptr<GameObject>>* worldMap, Static::Direction attackDir) {
	float pushBackMinDistance = 0;
	switch (attackDir){
	case Static::Direction::Up:{
		pushBackMinDistance = getMinimumLineCollisionDistance(Static::Up, worldMap);
		if (!isOutsideRoomBound(Point(position.x, position.y - pushBackMinDistance))){
				if (dir==Static::Up)
					pushbackStep += pushBackMinDistance;
				else if (dir==Static::Down)
					pushbackStep -= pushBackMinDistance;
		}
		else//the maximum pushback is beyond the room bounds
		{
			if (dir == Static::Up)
				pushbackStep += getDistanceToMapBoundary(Static::Up);
			else if (dir == Static::Down)
				pushbackStep -= getDistanceToMapBoundary(Static::Up);
		}
		break;
	}
	case Static::Direction::Down:{
		pushBackMinDistance = getMinimumLineCollisionDistance(Static::Down, worldMap);
		if (!isOutsideRoomBound(Point(position.x, position.y + pushBackMinDistance))){
			if (dir == Static::Up)
				pushbackStep -= pushBackMinDistance;
			else if (dir == Static::Down)
				pushbackStep += pushBackMinDistance;
		}
		else
		{
			if (dir == Static::Up)
				pushbackStep -= getDistanceToMapBoundary(Static::Down);
			else if (dir == Static::Down)
				pushbackStep += getDistanceToMapBoundary(Static::Down);
		}
		break;
	}	
	case Static::Direction::Right:{
		pushBackMinDistance = getMinimumLineCollisionDistance(Static::Right, worldMap);
		if (!isOutsideRoomBound(Point(position.x + pushBackMinDistance, position.y))){
				if (dir == Static::Left)
					pushbackStep -= pushBackMinDistance;
				else if (dir == Static::Right)
					pushbackStep += pushBackMinDistance;
		}
		else
		{
			if (dir == Static::Left)
				pushbackStep -= getDistanceToMapBoundary(Static::Right);
			else if (dir == Static::Right)
				pushbackStep += getDistanceToMapBoundary(Static::Right);
		}
		break;
	}
	case Static::Direction::Left:{
		pushBackMinDistance = getMinimumLineCollisionDistance(Static::Left, worldMap);
			if (!isOutsideRoomBound(Point(position.x - pushBackMinDistance, position.y))){
				if (dir == Static::Left)
					pushbackStep += pushBackMinDistance;
				else if (dir == Static::Right)
					pushbackStep -= pushBackMinDistance;
			}
			else
			{
				if (dir == Static::Left)
					pushbackStep += getDistanceToMapBoundary(Static::Left);
				else if (dir == Static::Right)
					pushbackStep -= getDistanceToMapBoundary(Static::Left);
			}
		break;
	}
	}
}
int Octorok::getMinimumLineCollisionDistance(Static::Direction pushbackDir, std::vector<std::shared_ptr<GameObject>>* worldMap) {
	float pushBackMinDistance = 0;
	sf::Vector2f size(width, height);
	std::unique_ptr<sf::RectangleShape> pushbackLineCheck = std::make_unique<sf::RectangleShape>();
	pushbackLineCheck->setPosition(position.x, position.y);
	pushbackLineCheck->setSize(size);
	Point pt;
	for (pushBackMinDistance = 0; pushBackMinDistance < pushBackMaxDistance; pushBackMinDistance++){
		if (pushbackDir==Static::Up)
			pt.setPoint(0, -pushBackMinDistance);
		else if (pushbackDir == Static::Down)
			pt.setPoint(0, pushBackMinDistance);
		else if (pushbackDir == Static::Right)
			pt.setPoint(pushBackMinDistance, 0);
		else if(pushbackDir == Static::Left)
			pt.setPoint(-pushBackMinDistance, 0);
		if (isColliding(worldMap, pushbackLineCheck, pt))
			break;
	}
	return pushBackMinDistance;
}
/*
Used to determine the distance to pushback in the case where there is no object in line such 
as map boundaries, otherwise the pushback return false because the max  pushback distance is 
outside the map boundaries.
*/
int Octorok::getDistanceToMapBoundary(Static::Direction direction){
	int worldX = (position.y-Global::inventoryHeight) / Global::roomHeight;
	int worldY = position.x / Global::roomWidth;
	int boundary;
	switch (direction){
	case Static::Direction::Left:
		boundary = worldY*Global::roomWidth;
		return position.x - boundary;
		break;
	case Static::Direction::Right:
		boundary = worldY*Global::roomWidth + Global::roomWidth -Global::TileWidth;
		return boundary - position.x;
		break;
	case Static::Direction::Down:
		boundary = worldX*Global::roomHeight +Global::roomHeight+Global::inventoryHeight- Global::TileHeight;
		return  boundary - position.y;
		break;
	case Static::Direction::Up:
		boundary = worldX*Global::roomHeight + Global::inventoryHeight;
		return position.y-boundary;
		break;
	}
}
bool Octorok::isColliding(std::vector<std::shared_ptr<GameObject>>* worldMap, std::unique_ptr<sf::RectangleShape>& mask, Point offsets) {
	bool collision = false;
	for(auto& obj : *worldMap)
	{
		if (dynamic_cast<Tile*>(obj.get()))
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
void Octorok::movement(std::vector<std::shared_ptr<GameObject>>* worldMap) {
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
	walkingAnimation.push_back(std::make_unique<Animation>("RedOctorok_Movement", height, width, position, 8));
	walkingAnimation.push_back(std::make_unique<Animation>("RedOctorok_Hit1", height, width, position, 8));
	walkingAnimation.push_back(std::make_unique<Animation>("RedOctorok_Hit2", height, width, position, 8));
}
