#include "Item\Bomb.h"
#include <iostream>
Bomb::~Bomb(){}
Bomb::Bomb(Point position,std::string name):super(position,name){
	width = 32;
	height = 32;
}
void Bomb::onUse(PlayerInfo info, std::vector<GameObject*>* worldMap){
	std::cout << "Throw Bomb";
	if (*info.bombAmount >= 1){
		*info.bombAmount -= 1;
		ThrownBomb* myBomb = new ThrownBomb(info.point,info.dir);
		Static::toAdd.push_back(myBomb);
	}
}