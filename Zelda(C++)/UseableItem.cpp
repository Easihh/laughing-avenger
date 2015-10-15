#include "UseableItem.h"

UseableItem::~UseableItem(){}
UseableItem::UseableItem(float x,float y,std::string item):super(x,y,item){
}
void UseableItem::onUse(){}