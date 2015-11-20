#include "Item\Bomb.h"
#include <iostream>
#include "Player\Player.h"
Bomb::Bomb(Point position,std::string name):super(position,name){
	width = 32;
	height = 32;
	isActive = true;
}
void Bomb::onUse(Point pos, std::vector<std::shared_ptr<GameObject>>* worldMap,Direction dir) {
	Player* tmp = (Player*)findPlayer(worldMap).get();
	std::cout << "Throw Bomb";
	if(tmp->inventory->playerBar->bombAmount >= 1){
		tmp->inventory->playerBar->bombAmount -= 1;
		std::shared_ptr<GameObject> myBomb = std::make_shared<ThrownBomb>(pos,dir);
		Sound::playSound(GameSound::BombDrop);
		Static::toAdd.push_back(myBomb);
	}
	if(tmp->inventory->playerBar->bombAmount == 0) isActive = false;
}