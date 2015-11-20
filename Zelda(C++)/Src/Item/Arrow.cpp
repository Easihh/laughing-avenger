#include "Item\Arrow.h"
#include "Item\ThrownArrow.h"
#include <iostream>
#include "Player\Player.h"
Arrow::Arrow(Point position, std::string name) :super(position, name){
	width = 32;
	height = 32;
	isActive = true;
}
void Arrow::onUse(Point pos, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction dir) {
	Player* tmp = (Player*)findPlayer(worldMap).get();
	std::cout << "Thrown Arrow";
	if(tmp->inventory->playerBar->diamondAmount >= 1){
		tmp->inventory->playerBar->diamondAmount -= 1;
		std::shared_ptr<GameObject> myArrow = std::make_shared<ThrownArrow>(pos,dir);
		Sound::playSound(GameSound::ArrowThrown);
		Static::toAdd.push_back(myArrow);
	}
	if(tmp->inventory->playerBar->diamondAmount == 0) isActive = false;
}