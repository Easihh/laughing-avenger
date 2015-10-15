#ifndef USEABLEITEM_H
#define USEABLEITEM_H
#include "Item.h"
#include <string>
class UseableItem:public Item{
public:
	UseableItem(float x,float y,std::string item);
	~UseableItem();
	typedef Item super;
private:
	virtual void onUse();
};
#endif