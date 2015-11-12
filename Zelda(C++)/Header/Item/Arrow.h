#ifndef ARROW_H
#define ARROW_H
#include "Item.h"
#include "ThrownBomb.h"
class Arrow :public Item {
public:
	Arrow(Point position, std::string name);
	typedef Item super;
	void onUse(PlayerInfo info, std::vector<std::shared_ptr<GameObject>>* worldMap);
private:
};
#endif