#include "Misc\ShopObject.h"

void ShopObject::resetShopItem() {
	isVisible = false;
	isObtained = false;
	position = origin;
}