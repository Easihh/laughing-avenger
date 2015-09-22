#include "Tile.h"

Tile::Tile(float x,float y){
	width = 32;
	height = 32;
	xPosition = x;
	yPosition = y;
	sprite.setSize(sf::Vector2f(32, 32));
	sprite.setOutlineColor(sf::Color::Magenta);
	sprite.setFillColor(sf::Color::Red);
	sprite.setOutlineThickness(1);
	sprite.setPosition(xPosition, yPosition);
}
Tile::~Tile(){}
void Tile::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
}
void Tile::update(){

}
