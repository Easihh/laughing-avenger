#include "Bomb.h"
#include <iostream>
Bomb::~Bomb(){}
Bomb::Bomb(float x,float y,std::string name):super(x,y,name){
	isActive = false;
}
void Bomb::onUse(float x,float y){
	std::cout << "Throw Bomb";
	sprite.setPosition(x, y);
	//playerInfo.playerBar->bombAmount--;
	isActive = true;
}
void Bomb::draw(sf::RenderWindow& mainWindow){
	if (isActive)
		mainWindow.draw(sprite);
}