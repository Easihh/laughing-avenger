#include "Item.h"
#include <string>
#include <iostream>
Item::~Item(){}
Item::Item(){}
Item::Item(float x,float y,std::string item){
	x = xPosition;
	y = yPosition;
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
void Item::onUse(PlayerInfo info){
	std::cout << "Item Used";
}