#include "Sword.h"
#include <string>
#include "Monster.h"
#include <iostream>
Sword::Sword(float playerX,float playerY,Static::Direction dir){
	xPosition = playerX;
	yPosition = playerY;
	swordCurrentFrame = 0;
	swordDelay = 0;
	loadImage(dir);
	setupFullMask();
	strength = 1;
}
Sword::~Sword(){}
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
	width = sprite.getTextureRect().width;
	height = sprite.getTextureRect().height;
	sprite.setPosition(xPosition, yPosition);
}
void Sword::update(bool& isAttacking, bool& canAttack, GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]){
	if (isAttacking){
		swordCurrentFrame++;
		if (isCollidingWithMonster(worldLayer));
			updateMonster();
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
bool Sword::isCollidingWithMonster(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]){
	collidingMonsterList.clear();
	bool isColliding = false;
	int worldX=xPosition/Global::roomWidth;
	int worldY=yPosition/Global::roomHeight;
	float startX = worldX*Global::roomRows;
	float startY = worldY*Global::roomCols;
	for (int i = startY; i < startY + Global::roomCols; i++){
		for (int j = startX; j < startX + Global::roomRows; j++){
			if (worldLayer[i][j] != NULL){
				if (dynamic_cast<Monster*>(worldLayer[i][j]))
					if (intersect(fullMask, ((Monster*)worldLayer[i][j])->mask, 0, 0)){
						isColliding = true;
						collidingMonsterList.push_back(worldLayer[i][j]);
					}
			}
		}
	}
	return isColliding;
}
void Sword::updateMonster(){
	for each (GameObject* mstr in collidingMonsterList)
	{
		Monster* temp = (Monster*)mstr;
		temp->takeDamage(strength);
		//std::cout << "Collision at X:" << temp->xPosition << " Y:" << temp->yPosition<<std::endl;
	}
}
void Sword::endSword(){
	swordDelay = swordMaxDelay;
	swordCurrentFrame = swordMaxFrame;
}