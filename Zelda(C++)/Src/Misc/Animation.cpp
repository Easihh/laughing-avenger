#include "Misc\Animation.h"
#include <fstream>
#include <iostream>
Animation::Animation(std::string filename, int recHeight, int recWidth, float rectX, float rectY, int framerate){
	fRate = framerate;
	fCounter = 0;
	currentIndex = 0;
	texture.loadFromFile("Tileset/" + filename + ".PNG");
	maxCol = texture.getSize().x / recWidth;
	maxRow = texture.getSize().y / recHeight;
	width = recWidth;
	height = recHeight;
	xPosition = rectX;
	yPosition = rectY;
	subRect.height = height;
	subRect.width = width;
	subRect.left = 0;
	subRect.top = 0;
	sprite.setTexture(texture);
	sprite.setTextureRect(subRect);
	sprite.setPosition(xPosition, yPosition);
}
Animation::Animation(){}
Animation::~Animation(){}
void Animation::updateAnimationFrame(Static::Direction dir,float x,float y){
	xPosition = x;
	yPosition = y;
	sprite.setPosition(xPosition, yPosition);
	fCounter++;
	if (fRate!=NULL && fCounter >= fRate){
		fCounter = 0;
		currentIndex++;
		if (currentIndex >= maxCol)
			currentIndex = 0;
	}
	setSubRectangle(dir);
}
void Animation::reset(){
		currentIndex = 0;
}
void Animation::setSubRectangle(Static::Direction dir){
	subRect.left = currentIndex*width;
	switch (dir)
	{
	case Static::Direction::Right:
		subRect.top = height * 3;
		break;
	case Static::Direction::Left:
		subRect.top = height * 2;
		break;
	case Static::Direction::Up:
		subRect.top = 0;
		break;
	case Static::Direction::Down:
		subRect.top = height;
		break;
	}
	sprite.setTextureRect(subRect);
}
