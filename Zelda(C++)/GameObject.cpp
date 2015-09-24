#include "GameObject.h"
#include "SFML\Graphics.hpp"
GameObject::GameObject(){}
GameObject::~GameObject(){}
void GameObject::update(GameObject* worldMap[Static::WorldRows][Static::WorldColumns]){
}
void GameObject::draw(sf::RenderWindow& mainWindow){
}
bool GameObject::intersect(GameObject* rectA,GameObject* rectB, int offsetX,int offsetY){
return(
rectA->xPosition + offsetX < rectB->xPosition + rectB->width  &&
rectA->xPosition + rectA->width +offsetX > rectB->xPosition &&
rectA->yPosition + offsetY < rectB->yPosition + rectB->height &&
rectA->yPosition + rectA->height +offsetY > rectB->yPosition);
}