#ifndef BOOMRANG_H
#define BOOMRANG_H
#include "Item.h"
class Boomrang :public Item{
public:
	~Boomrang();
	Boomrang(Point position, std::string name);
	typedef Item super;
	void onUse(PlayerInfo info, std::vector<std::shared_ptr<GameObject>>* worldMap);
private:
};
#endif