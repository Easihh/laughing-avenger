#include "Item\Boomrang.h"
#include <iostream>
#include "Utility\Static.h"
#include "Item\ThrownBoomrang.h"
#include "Player\Player.h"
Boomrang::Boomrang(Point pos, std::string name) :super(position, name){
	position = pos;
	width = 32;
	height = 32;
	isActive = true;
}
void Boomrang::onUse(Point pos, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction dir) {
	std::shared_ptr<GameObject> temp = findPlayer(worldMap);
	Player* player = (Player*)temp.get();
	if(!player->boomerangIsActive){
		std::cout << "Throw Boomrang";
		player->boomerangIsActive = true;
		std::shared_ptr<GameObject> myBoomerang = std::make_shared<ThrownBoomrang>(pos, dir);
		Static::toAdd.push_back(myBoomerang);
	}
}