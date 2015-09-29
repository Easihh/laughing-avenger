#include "Sword.h"
#include <string>
Sword::Sword(float playerX,float playerY,Static::Direction dir){
	xPosition = playerX;
	yPosition = playerY;
	swordMaxFrame = 30;
	swordCurrentFrame = 0;
	swordDelay = 0;
	swordMaxDelay = 16;
	loadImage(dir);
}
Sword::~Sword(){

}
void Sword::loadImage(Static::Direction dir){
	switch (dir){
	case Static::Direction::Left:
		texture.loadFromFile("Tileset/WoodSword_Left.png");
		xPosition = xPosition - (Global::minGridStep) - (Global::minGridStep / 2);
		yPosition = yPosition + (Global::minGridStep/2);
		break;
	case Static::Direction::Right:
		texture.loadFromFile("Tileset/WoodSword_Right.png");
		xPosition = xPosition + (Global::minGridStep) + (Global::minGridStep / 2);
		yPosition = yPosition + (Global::minGridStep / 2);
		break;
	case Static::Direction::Down:
		texture.loadFromFile("Tileset/WoodSword_Down.png");
		xPosition = xPosition + (Global::minGridStep / 2);
		yPosition = yPosition + Global::minGridStep + (Global::minGridStep / 4);
		break;
	case Static::Direction::Up:
		texture.loadFromFile("Tileset/WoodSword_Up.png");
		xPosition = xPosition + (Global::minGridStep / 4);
		yPosition = yPosition - Global::minGridStep - (Global::minGridStep / 2);
		break;
	}	
	sprite.setTexture(texture);
	sprite.setPosition(xPosition, yPosition);
}
void Sword::update(bool& isAttacking,bool& canAttack){
	if (isAttacking){
		swordCurrentFrame++;
		if (swordCurrentFrame >= swordMaxFrame){
		isAttacking = false;
		swordCurrentFrame = 0;
		}
	}
	if (!isAttacking && !canAttack){
		swordDelay++;
		if (swordDelay >= swordMaxDelay){
			swordDelay = 0;
			canAttack = true;
		}
	}
}