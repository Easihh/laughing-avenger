#include "Misc\ShopObject.h"
#include "Utility\Static.h"
void ShopObject::resetShopItem() {
	isVisible = false;
	isObtained = false;
	position = origin;
}
void ShopObject::hideOtherShopItems(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	for(int i = 0; i < Worldmap->size(); i++){
		GameObject* tmp = Worldmap->at(i).get();
		if(tmp != this && dynamic_cast<ShopObject*>(tmp))
			((ShopObject*)tmp)->resetShopItem();
	}
}