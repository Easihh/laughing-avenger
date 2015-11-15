#include "Item\Candle.h"
#include "Player\Player.h"
#include <iostream>
#include "Misc\CandleFlame.h"
Candle::Candle(Point position, std::string name) :super(position, name) {
	width = 32;
	height = 32;
	isActive = true;
}
void Candle::destroyOtherFlame(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	for(int i = 0; i < worldMap->size(); i++){
		if(dynamic_cast<CandleFlame*>(worldMap->at(i).get()))
			Static::toDelete.push_back(worldMap->at(i));
	}
}
void Candle::onUse(Point pos, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction dir) {
	Player* tmp = (Player*)findPlayer(worldMap).get();
	std::cout << "Candle Used";
	destroyOtherFlame(worldMap);
	std::shared_ptr<CandleFlame> fire = std::make_shared<CandleFlame>(tmp->position,dir);
	Static::toAdd.push_back(fire);
}