#ifndef ARROW_H
#define ARROW_H
#include "Item.h"
#include "ThrownBomb.h"
class Arrow :public Item {
public:
	Arrow(Point position, std::string name);
	typedef Item super;
	void onUse(Point pos, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction dir);
private:
};
#endif