#include "Sword.h"
#include <string>
#include "Monster.h"
#include <iostream>
Sword::Sword(float playerX,float playerY,Static::Direction dir){
	xPosition = playerX;
	yPosition = playerY;
	swordCurrentFrame = 0;
	swordDelay = 0;
	swordDir = dir;
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
void Sword::update(bool& isAttacking, bool& canAttack, std::vector<GameObject*> worldMap, Animation* walkAnimation[3]){
	if (isAttacking){
		swordCurrentFrame++;
		if (isCollidingWithMonster(worldMap));
			updateMonster();
		if (swordCurrentFrame >= swordMaxFrame){
			isAttacking = false;
			swordCurrentFrame = 0;
			for (int i = 0; i < 3; i++){
				walkAnimation[i]->reset();
				walkAnimation[i]->setSubRectangle(swordDir);
			}
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
bool Sword::isCollidingWithMonster(std::vector<GameObject*> worldMap){
	collidingMonsterList.clear();
	bool isColliding = false;	
	for each (GameObject* obj in worldMap)
	{
			if (dynamic_cast<Monster*>(obj))
				if (intersect(fullMask, ((Monster*)obj)->fullMask, 0, 0)){
					isColliding = true;
					collidingMonsterList.push_back(obj);
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