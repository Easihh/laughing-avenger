#include "Item\Item.h"
#include <string>
#include <iostream>
Item::~Item(){}
Item::Item(){}
Item::Item(Point position,std::string item){
	xPosition = position.x;
	yPosition = position.y;
	itemName=item;
	loadImage();
}
void Item::loadImage(){
	if (!texture.loadFromFile("Tileset/" + itemName + ".png"))
		std::cout << "Failed to load:" << itemName << std::endl;
	sprite.setTexture(texture);
}
void Item::update(std::vector<GameObject*> worldMap){

}
void Item::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
}
void Item::onUse(PlayerInfo info, std::vector<GameObject*>* worldMap){
	std::cout << "Item Used";
}