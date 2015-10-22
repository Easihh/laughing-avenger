#include "Misc\GameObject.h"
#include "SFML\Graphics.hpp"
#include<iostream>
GameObject::GameObject(){}
GameObject::~GameObject(){}
void GameObject::update(std::vector<GameObject*>* worldMap){
}
void GameObject::draw(sf::RenderWindow& mainWindow){
}
bool GameObject::intersect(sf::RectangleShape* rectA, sf::RectangleShape* rectB, Point offset){
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
	fullMask = new sf::RectangleShape();
	sf::Vector2f size(width, height);
	fullMask->setSize(size);
	fullMask->setOutlineThickness(1);
	fullMask->setFillColor(sf::Color::Transparent);
	fullMask->setPosition(xPosition, yPosition);
	fullMask->setOutlineColor(sf::Color::Black);
}