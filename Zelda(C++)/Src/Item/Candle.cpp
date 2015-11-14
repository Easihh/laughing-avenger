#include "Item\Candle.h"
#include "Player\Player.h"
#include <iostream>
Candle::Candle(Point position, std::string name) :super(position, name) {
	width = 32;
	height = 32;
	isActive = true;
}
void Candle::onUse(Point pos, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction dir) {
	Player* tmp = (Player*)findPlayer(worldMap).get();
	std::cout << "Candle Used";
}