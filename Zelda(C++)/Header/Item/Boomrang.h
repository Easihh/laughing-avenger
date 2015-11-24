#ifndef BOOMRANG_H
#define BOOMRANG_H
#include "Item.h"
#include "Misc\Animation.h"
class Boomrang :public Item{
public:
	Boomrang(Point position, std::string name);
	typedef Item super;
	void onUse(Point pos, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction dir);
private:
};
#endif