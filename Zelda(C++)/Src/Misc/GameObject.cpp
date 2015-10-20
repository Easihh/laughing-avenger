#include "Misc\GameObject.h"
#include "SFML\Graphics.hpp"
GameObject::GameObject(){}
GameObject::~GameObject(){}
void GameObject::update(std::vector<GameObject*>* worldMap){
}
void GameObject::draw(sf::RenderWindow& mainWindow){
}
bool GameObject::intersect(sf::RectangleShape* rectA, sf::RectangleShape* rectB, int offsetX, int offsetY){
	float rectAx		= rectA->getPosition().x;
	float rectAxSize	= rectA->getSize().x;
	float rectAySize	= rectA->getSize().y;
	float rectAy		= rectA->getPosition().y;
	float rectBx		= rectB->getPosition().x;
	float rectBxSize	= rectB->getSize().x;
	float rectBySize	= rectB->getSize().y;
	float rectBy		= rectB->getPosition().y;
return(
	rectAx + offsetX < rectBx + rectBxSize  &&
	rectAx + rectAxSize + offsetX > rectBx &&
	rectAy + offsetY < rectBy + rectBySize &&
	rectAy + rectAySize + offsetY > rectBy);
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