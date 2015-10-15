#ifndef USEABLEBOMB_H
#define USEABLEBOMB_H
#include "UseableItem.h"
class UseableBomb :public UseableItem{
public:
	UseableBomb(float x,float y,std::string item);
	~UseableBomb();
	typedef UseableItem super;
private:
	void onUse();
};
#endif