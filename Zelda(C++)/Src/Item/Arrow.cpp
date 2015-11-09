#include "Item\Arrow.h"
#include "Item\ThrownArrow.h"
#include <iostream>
Arrow::~Arrow() {}
Arrow::Arrow(Point position, std::string name) :super(position, name){
	width = 32;
	height = 32;
	isActive = true;
}
void Arrow::onUse(PlayerInfo info, std::vector<std::shared_ptr<GameObject>>* worldMap) {
	std::cout << "Thrown Arrow";
	if(*info.diamondAmount >= 1){
		*info.diamondAmount -= 1;
		std::shared_ptr<GameObject> myArrow = std::make_shared<ThrownArrow>(info.point, info.dir);
		Sound::playSound(ArrowThrown);
		Static::toAdd.push_back(myArrow);
	}
	if(*info.diamondAmount == 0) isActive = false;
}