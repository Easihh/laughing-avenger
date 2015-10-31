#include "Item\Boomrang.h"
#include <iostream>
Boomrang::~Boomrang(){}
Boomrang::Boomrang(Point position, std::string name) :super(position, name){
	width = 32;
	height = 32;
	isActive = true;
}
void Boomrang::onUse(PlayerInfo info, std::vector<GameObject*>* worldMap){
	std::cout << "Throw Boomrang";
}