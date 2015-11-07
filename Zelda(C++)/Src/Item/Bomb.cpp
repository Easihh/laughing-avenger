#include "Item\Bomb.h"
#include <iostream>
Bomb::~Bomb(){}
Bomb::Bomb(Point position,std::string name):super(position,name){
	width = 32;
	height = 32;
	isActive = true;
}
void Bomb::onUse(PlayerInfo info, std::vector<std::shared_ptr<GameObject>>* worldMap) {
	std::cout << "Throw Bomb";
	if (*info.bombAmount >= 1){
		*info.bombAmount -= 1;
		std::shared_ptr<GameObject> myBomb = std::make_shared<ThrownBomb>(info.point, info.dir);
		Sound::playSound(BombDrop);
		Static::toAdd.push_back(myBomb);
	}
	if (*info.bombAmount==0) isActive = false;
}