#include "Block.h"

Block::Block(){
	width = 32;
	height = 32;
	xPosition = 128;
	yPosition = 128;
}
Block::~Block(){

}
void Block::draw(sf::RenderWindow& mainWindow){
	sprite.setSize(sf::Vector2f(width, height));
	sprite.setOutlineColor(sf::Color::Magenta);
	sprite.setFillColor(sf::Color::Black);
	sprite.setOutlineThickness(1);
	sprite.setPosition(xPosition, yPosition);
	mainWindow.draw(sprite);
}
void Block::update(){

}
