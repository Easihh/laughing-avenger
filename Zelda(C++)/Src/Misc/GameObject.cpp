#include "Misc\GameObject.h"
#include "SFML\Graphics.hpp"
#include<iostream>
#include"Utility\Static.h"
#include "Player\MovingSword.h"
#include "Player\Player.h"
GameObject::GameObject(){}
GameObject::~GameObject(){}
void GameObject::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {}
void GameObject::draw(sf::RenderWindow& mainWindow){}
void GameObject::destroyGameObject(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	std::shared_ptr<GameObject> del;
	for(auto& obj : *Worldmap){
		if(obj.get() == this)
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
void GameObject::setupFullMask(){
	fullMask = std::make_unique<sf::RectangleShape>();
	sf::Vector2f size(width, height);
	fullMask->setSize(size);
	fullMask->setOutlineThickness(1);
	fullMask->setFillColor(sf::Color::Transparent);
	fullMask->setPosition(position.x, position.y);
	fullMask->setOutlineColor(sf::Color::Magenta);
}
bool GameObject::isCollidingWithMonster(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	bool isColliding = false;
	Point offset(0, 0);
	for(auto& obj : *worldMap)
	{
		if(dynamic_cast<Monster*>(obj.get()))
			if(intersect(fullMask, ((Monster*)obj.get())->mask, offset)){
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