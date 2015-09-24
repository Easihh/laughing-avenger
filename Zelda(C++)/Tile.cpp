#include "Tile.h"

Tile::Tile(float x, float y,bool canBeCollidedWith){
	xPosition = x;
	yPosition = y;
	isCollideable = canBeCollidedWith;
	texture.loadFromFile("Tileset/Sand.png");
	sprite.setTexture(texture);
	sprite.setPosition(xPosition, yPosition);
}
Tile::~Tile(){}
void Tile::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
}
void Tile::update(){

}