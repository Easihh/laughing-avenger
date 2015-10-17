#include "Bomb.h"
#include <iostream>
Bomb::~Bomb(){}
Bomb::Bomb(float x,float y,std::string name):super(x,y,name){
	isActive = false;
	width = 32;
	height = 32;
}
void Bomb::onUse(PlayerInfo info){
	std::cout << "Throw Bomb";
	switch (info.dir)
	{
	case Static::Direction::Down:
		sprite.setPosition(info.point.x, info.point.y + height);
		break;
	case Static::Direction::Up:
		sprite.setPosition(info.point.x, info.point.y-height);
		break;
	case Static::Direction::Right:
		sprite.setPosition(info.point.x + width, info.point.y);
		break;
	case Static::Direction::Left:
		sprite.setPosition(info.point.x-width, info.point.y);
		break;
	}
	if (*info.bombAmount >= 1){
		*info.bombAmount -= 1;
		isActive = true;
	}
	else isActive = false;
}
void Bomb::draw(sf::RenderWindow& mainWindow){
	if (isActive)
		mainWindow.draw(sprite);
}