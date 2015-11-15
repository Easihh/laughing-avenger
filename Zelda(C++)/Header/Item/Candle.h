#ifndef CANDLE_H
#define CANDLE_H
#include "Item.h"
class Candle :public Item {
public:
	Candle(Point position, std::string name);
	typedef Item super;
	void onUse(Point pos, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction dir);
private:
	void destroyOtherFlame(std::vector<std::shared_ptr<GameObject>>* Worldmap);
};
#endif