#ifndef BOMB_H
#define BOMB_H
#include "Item.h"
#include "ThrownBomb.h"
class Bomb :public Item{
public:
	Bomb(Point position,std::string name);
	typedef Item super;
	void onUse(PlayerInfo info, std::vector<std::shared_ptr<GameObject>>* worldMap);
private:
};
#endif