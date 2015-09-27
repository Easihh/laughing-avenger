#include "Sword.h"
#include <string>
Sword::Sword(float playerX,float playerY,Static::Direction dir){
	xPosition = playerX;
	yPosition = playerY;
	loadImage(dir);
}
Sword::~Sword(){

}
void Sword::loadImage(Static::Direction dir){
	switch (dir){
	case Static::Direction::Left:
		texture.loadFromFile("Tileset/WoodSword_Left.png");
		xPosition = xPosition - (Global::minStep ) -(Global::minStep /2);
		yPosition = yPosition + (Global::minStep/2);
		break;
	case Static::Direction::Right:
		texture.loadFromFile("Tileset/WoodSword_Right.png");
		xPosition = xPosition + (Global::minStep)+(Global::minStep/2);
		yPosition = yPosition + (Global::minStep / 2);
		break;
	case Static::Direction::Down:
		texture.loadFromFile("Tileset/WoodSword_Down.png");
		xPosition = xPosition + (Global::minStep / 2);
		yPosition = yPosition + Global::minStep + (Global::minStep / 4);
		break;
	case Static::Direction::Up:
		texture.loadFromFile("Tileset/WoodSword_Up.png");
		xPosition = xPosition + (Global::minStep / 4);
		yPosition = yPosition - Global::minStep - (Global::minStep / 2);
		break;
	}	
	sprite.setTexture(texture);
	sprite.setPosition(xPosition, yPosition);
}
