#include "Item\Potion.h"
#include <iostream>
#include "Player\Player.h"
Potion::Potion(Point position, std::string name) :super(position, name){
	width = 32;
	height = 32;
	isActive = false;
}
void Potion::onUse(Point pos, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction dir) {
	Player* tmp = (Player*)findPlayer(worldMap).get();
	std::cout << "Use Potion";
}