#include "Item\Item.h"
#include <string>
#include <iostream>
Item::~Item(){}
Item::Item(){}
Item::Item(Point pos,std::string item){
	position = pos;
	itemName=item;
	loadImage();
}
void Item::loadImage(){
	if (!texture.loadFromFile("Tileset/" + itemName + ".png"))
		std::cout << "Failed to load:" << itemName << std::endl;
	sprite.setTexture(texture);
}
void Item::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {}
void Item::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
}
void Item::onUse(PlayerInfo info, std::vector<std::shared_ptr<GameObject>>* worldMap) {
	std::cout << "Item Used";
}