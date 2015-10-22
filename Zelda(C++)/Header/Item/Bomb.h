#ifndef BOMB_H
#define BOMB_H
#include "Item.h"
#include "ThrownBomb.h"
class Bomb :public Item{
public:
	~Bomb();
	Bomb(Point position,std::string name);
	typedef Item super;
	void onUse(PlayerInfo info, std::vector<GameObject*>* worldMap);
	ThrownBomb* myBomb;
private:
};
#endif