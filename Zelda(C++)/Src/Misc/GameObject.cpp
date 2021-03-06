#include "Misc\GameObject.h"
#include "SFML\Graphics.hpp"
#include<iostream>
#include"Utility\Static.h"
#include "Player\MovingSword.h"
#include "Player\Player.h"
#include "Misc\Tile.h"
#include "Misc\NPC.h"
#include "Item\ThrownBoomrang.h"
#include "Item\PotionPickUp.h"
#include "Item\HeartContainer.h"
#include "Monster\WallMasterSpawner.h"
GameObject::GameObject(){
	pushbackStep = 0;
	depth = 0;
}
void GameObject::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {}
void GameObject::draw(sf::RenderWindow& mainWindow){}
void GameObject::destroyHeartContainer(std::vector<std::shared_ptr<GameObject>>* Worldmap){
	std::shared_ptr<GameObject> del;
	for (auto& obj : *Worldmap){
		if (dynamic_cast<HeartContainer*>(obj.get()))
			del = obj;
	}
	Static::toDelete.push_back(del);
}
void GameObject::destroyPotion(std::vector<std::shared_ptr<GameObject>>* Worldmap){
	std::shared_ptr<GameObject> del;
	for (auto& obj : *Worldmap){
		if (dynamic_cast<PotionPickUp*>(obj.get()))
			del = obj;
	}
	Static::toDelete.push_back(del);
}
void GameObject::deleteNpcFromCurrentRoom(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	std::shared_ptr<GameObject> del;
	for(auto& obj : *Worldmap){
		if(dynamic_cast<NPC*>(obj.get()))
			del = obj;
	}
	Static::toDelete.push_back(del);
}
void GameObject::destroyGameObject(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	std::shared_ptr<GameObject> del;
	for(auto& obj : *Worldmap){
		if(obj.get() == this)
			del = obj;
	}
	Static::toDelete.push_back(del);
}
void GameObject::destroyGameObject(std::vector<std::shared_ptr<GameObject>>* Worldmap, std::shared_ptr<GameObject> del) {
	for (auto& obj : *Worldmap){
		if (obj.get() == this)
			del = obj;
	}
	Static::toDelete.push_back(del);
}
bool GameObject::intersect(std::unique_ptr<sf::RectangleShape>& rectA, std::unique_ptr<sf::RectangleShape>& rectB, Point offset) {
	float rectAx		= rectA->getPosition().x;
	float rectAxSize	= rectA->getSize().x;
	float rectAySize	= rectA->getSize().y;
	float rectAy		= rectA->getPosition().y;
	float rectBx		= rectB->getPosition().x;
	float rectBxSize	= rectB->getSize().x;
	float rectBySize	= rectB->getSize().y;
	float rectBy		= rectB->getPosition().y;
return(
	rectAx + offset.x < rectBx + rectBxSize  &&
	rectAx + rectAxSize + offset.x > rectBx &&
	rectAy + offset.y < rectBy + rectBySize &&
	rectAy + rectAySize + offset.y > rectBy);
}
bool GameObject::intersect(std::unique_ptr<sf::RectangleShape>& rectA, std::unique_ptr<sf::RectangleShape>& rectB) {
	float rectAx = rectA->getPosition().x;
	float rectAxSize = rectA->getSize().x;
	float rectAySize = rectA->getSize().y;
	float rectAy = rectA->getPosition().y;
	float rectBx = rectB->getPosition().x;
	float rectBxSize = rectB->getSize().x;
	float rectBySize = rectB->getSize().y;
	float rectBy = rectB->getPosition().y;
	return(
		rectAx  < rectBx + rectBxSize  &&
		rectAx + rectAxSize  > rectBx &&
		rectAy < rectBy + rectBySize &&
		rectAy + rectAySize > rectBy);
}
bool GameObject::isCollidingWithBoomerang(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	bool isColliding = false;
	Point offset(0, 0);
	for(auto& obj : *worldMap){
		if(dynamic_cast<ThrownBoomrang*>(obj.get())){
			if (intersect(fullMask, obj.get()->fullMask))
				isColliding = true;
		}
	}
	return isColliding;
}
bool GameObject::isCollidingWithPlayer(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	bool isColliding = false;
	Point offset(0, 0);
	Player* player =(Player*)findPlayer(worldMap).get();
	if(intersect(player->fullMask, mask, offset))
		isColliding = true;
	return isColliding;
}
void GameObject::pushBack(std::vector<std::shared_ptr<GameObject>>* worldMap, Direction attackDir) {
	float pushBackMinDistance = 0;
	switch(attackDir){
	case Direction::Up:{
		pushBackMinDistance = getMinimumLineCollisionDistance(Direction::Up, worldMap);
		if(!isOutsideRoomBound(Point(position.x, position.y - pushBackMinDistance))){
			if(dir == Direction::Up)
				pushbackStep += pushBackMinDistance;
			else if(dir == Direction::Down)
				pushbackStep -= pushBackMinDistance;
		}
		else//the maximum pushback is beyond the room bounds
		{
			if(dir == Direction::Up)
				pushbackStep += getDistanceToMapBoundary(Direction::Up);
			else if(dir == Direction::Down)
				pushbackStep -= getDistanceToMapBoundary(Direction::Up);
		}
		break;
	}
	case Direction::Down:{
		pushBackMinDistance = getMinimumLineCollisionDistance(Direction::Down, worldMap);
		if(!isOutsideRoomBound(Point(position.x, position.y + pushBackMinDistance))){
			if(dir == Direction::Up)
				pushbackStep -= pushBackMinDistance;
			else if(dir == Direction::Down)
				pushbackStep += pushBackMinDistance;
		}
		else
		{
			if(dir == Direction::Up)
				pushbackStep -= getDistanceToMapBoundary(Direction::Down);
			else if(dir == Direction::Down)
				pushbackStep += getDistanceToMapBoundary(Direction::Down);
		}
		break;
	}
	case Direction::Right:{
		pushBackMinDistance = getMinimumLineCollisionDistance(Direction::Right, worldMap);
		if(!isOutsideRoomBound(Point(position.x + pushBackMinDistance, position.y))){
			if(dir == Direction::Left)
				pushbackStep -= pushBackMinDistance;
			else if(dir == Direction::Right)
				pushbackStep += pushBackMinDistance;
		}
		else
		{
			if(dir == Direction::Left)
				pushbackStep -= getDistanceToMapBoundary(Direction::Right);
			else if(dir == Direction::Right)
				pushbackStep += getDistanceToMapBoundary(Direction::Right);
		}
		break;
	}
	case Direction::Left:{
		pushBackMinDistance = getMinimumLineCollisionDistance(Direction::Left, worldMap);
		if(!isOutsideRoomBound(Point(position.x - pushBackMinDistance, position.y))){
			if(dir == Direction::Left)
				pushbackStep += pushBackMinDistance;
			else if(dir == Direction::Right)
				pushbackStep -= pushBackMinDistance;
		}
		else
		{
			if(dir == Direction::Left)
				pushbackStep += getDistanceToMapBoundary(Direction::Left);
			else if(dir == Direction::Right)
				pushbackStep -= getDistanceToMapBoundary(Direction::Left);
		}
		break;
	}
	}
}
void GameObject::pushbackUpdate() {
	//positive pushback mean same direction as currently facing.
	int step = stepPerPushBackUpdate;
	if(std::abs(pushbackStep) < stepPerPushBackUpdate)
		step = std::abs(pushbackStep);
	switch(dir){
	case Direction::Down:
	if(pushbackStep < 0)
		position.y -= step;
	else position.y += step;
	break;
	case Direction::Up:
	if(pushbackStep < 0)
		position.y += step;
	else position.y -= step;
	break;
	case Direction::Left:
	if(pushbackStep < 0)
		position.x += step;
	else position.x -= step;
	break;
	case Direction::Right:
	if(pushbackStep < 0)
		position.x -= step;
	else position.x += step;
	break;
	}
	if(pushbackStep>0)
		pushbackStep -= step;
	else pushbackStep += step;
}
/*
Used to determine the distance to pushback in the case where there is no object in line such
as map boundaries, otherwise the pushback return false because the max  pushback distance is
outside the map boundaries.
*/
int GameObject::getDistanceToMapBoundary(Direction direction) {
	int worldX = (position.y - Global::inventoryHeight) / Global::roomHeight;
	int worldY = position.x / Global::roomWidth;
	int boundary;
	switch(direction){
	case Direction::Left:
		boundary = worldY*Global::roomWidth;
		return position.x - boundary;
	break;
	case Direction::Right:
		boundary = worldY*Global::roomWidth + Global::roomWidth - Global::TileWidth;
		return boundary - position.x;
	break;
	case Direction::Down:
		boundary = worldX*Global::roomHeight + Global::roomHeight + Global::inventoryHeight - Global::TileHeight;
		return  boundary - position.y;
	break;
	case Direction::Up:
		boundary = worldX*Global::roomHeight + Global::inventoryHeight;
		return position.y - boundary;
	break;
	}
}
int GameObject::getMinimumLineCollisionDistance(Direction pushbackDir, std::vector<std::shared_ptr<GameObject>>* worldMap) {
	float pushBackMinDistance = 0;
	sf::Vector2f size(width, height);
	std::unique_ptr<sf::RectangleShape> pushbackLineCheck = std::make_unique<sf::RectangleShape>();
	pushbackLineCheck->setPosition(position.x, position.y);
	pushbackLineCheck->setSize(size);
	Point pt;
	for(pushBackMinDistance = 0; pushBackMinDistance < pushBackMaxDistance; pushBackMinDistance++){
		if(pushbackDir == Direction::Up)
			pt.setPoint(0, -pushBackMinDistance);
		else if(pushbackDir == Direction::Down)
			pt.setPoint(0, pushBackMinDistance);
		else if(pushbackDir == Direction::Right)
			pt.setPoint(pushBackMinDistance, 0);
		else if(pushbackDir == Direction::Left)
			pt.setPoint(-pushBackMinDistance, 0);
		if(isColliding(worldMap, pushbackLineCheck, pt))
			break;
	}
	return pushBackMinDistance;
}
bool GameObject::isColliding(std::vector<std::shared_ptr<GameObject>>* worldMap, std::unique_ptr<sf::RectangleShape>& mask, Point offsets) {
	bool collision = false;
	for(auto& obj : *worldMap)
	{
		if(dynamic_cast<Tile*>(obj.get()))
			if(intersect(fullMask, obj->fullMask, offsets)){
				collision = true;
				std::cout << "CollisionX:" << obj->position.x << std::endl;
				std::cout << "CollisionY:" << obj->position.y << std::endl;
				std::cout << "X:" << position.x << std::endl;
				std::cout << "Y:" << position.y << std::endl;
			}
	}
	return collision;
}
void GameObject::setupMask(std::unique_ptr<sf::RectangleShape>* themask, int theWidth, int theHeight, sf::Color color){
	*themask = std::make_unique<sf::RectangleShape>();
	sf::Vector2f size(theWidth, theHeight);
	themask->get()->setSize(size);
	themask->get()->setOutlineThickness(1);
	themask->get()->setFillColor(sf::Color::Transparent);
	themask->get()->setPosition(position.x, position.y);
	themask->get()->setOutlineColor(color);
}
bool GameObject::isCollidingWithMonster(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	bool isColliding = false;
	Point offset(0, 0);
	for(auto& obj : *worldMap)
	{
		if(dynamic_cast<Monster*>(obj.get()))
			if(intersect(fullMask, ((Monster*)obj.get())->fullMask, offset)){
				isColliding = true;
				collidingMonster = obj;
				break;
			}
	}
	return isColliding;
}
bool GameObject::isOutsideRoomBound(Point pos) {
	int worldX = (position.y - Global::inventoryHeight) / Global::roomHeight;
	int worldY = position.x / Global::roomWidth;
	bool outsideBoundary = false;
	if(pos.x + width > (Global::roomWidth*worldY) + Global::roomWidth)
		outsideBoundary = true;
	else if(pos.x < (Global::roomWidth*worldY))
		outsideBoundary = true;
	else if(pos.y< (Global::roomHeight*worldX) + Global::inventoryHeight)
		outsideBoundary = true;
	else if(pos.y + height >(Global::roomHeight*worldX) + Global::roomHeight + Global::inventoryHeight)
		outsideBoundary = true;
	return outsideBoundary;
}
std::shared_ptr<GameObject> GameObject::findPlayer(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	for(int i = 0; i < worldMap->size(); i++){
		if(dynamic_cast<Player*>(worldMap->at(i).get()))
			return worldMap->at(i);
	}
}
std::shared_ptr<GameObject> GameObject::findClosestSpawner(std::vector<std::shared_ptr<GameObject>>* worldMap,Point objPos){
	std::shared_ptr<GameObject> closest;
	float minDistance = 9999;
	for (int i = 0; i < worldMap->size(); i++){
		if (dynamic_cast<WallMasterSpawner*>(worldMap->at(i).get())){
			float distance = distanceBetweenPoint(worldMap->at(i).get()->position, objPos);
			if (distance < minDistance){
				closest = worldMap->at(i);
				minDistance = distance;
			}
		}
	}
	return closest;
}
float GameObject::distanceBetweenPoint(Point pt1, Point pt2){
	float distance=0;
	distance = std::pow((pt1.x - pt2.x), 2) +std::pow((pt1.y - pt2.y),2);
	distance = std::sqrt(distance);
	return distance;
}
std::shared_ptr<GameObject> GameObject::findBoomerang(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	for (int i = 0; i < worldMap->size(); i++){
		if (dynamic_cast<ThrownBoomrang*>(worldMap->at(i).get()))
			return worldMap->at(i);
	}
}