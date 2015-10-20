#include "Item\Bomb.h"
#include <iostream>
Bomb::~Bomb(){}
Bomb::Bomb(float x,float y,std::string name):super(x,y,name){
	width = 32;
	height = 32;
}
void Bomb::onUse(PlayerInfo info, std::vector<GameObject*>* worldMap){
	std::cout << "Throw Bomb";
	if (*info.bombAmount >= 1){
		*info.bombAmount -= 1;
		myBomb = new ThrownBomb(info.point.x, info.point.y,info.dir);
		worldMap->push_back(myBomb);
	}
}