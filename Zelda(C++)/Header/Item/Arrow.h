#ifndef ARROW_H
#define ARROW_H
#include "Item.h"
class Arrow :public Item {
public:
	Arrow(Point position, std::string name);
	typedef Item super;
	bool bowIsActive;
	void onUse(Point pos, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction dir);
private:
};
#endif