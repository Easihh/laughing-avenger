#include "Tile.h"

Tile::Tile(float x,float y){
	xPosition = x;
	yPosition = y;
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
