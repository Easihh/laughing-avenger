#ifndef POTION_H
#define POTION_H
#include "Item.h"
class Potion :public Item {
public:
	Potion(Point position, std::string name);
	typedef Item super;
	void onUse(Point pos, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction dir);
private:
	void destroyOtherFlame(std::vector<std::shared_ptr<GameObject>>* Worldmap);
};
#endif