#include "Player\Sword.h"
#include <string>
#include "Monster\Monster.h"
#include <iostream>
Sword::Sword(Point pos,Static::Direction dir){
	position = pos;
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
		position.x = position.x - (Global::minGridStep) - (Global::minGridStep / 2);
		position.y = position.y + (Global::minGridStep / 2);
		break;
	case Static::Direction::Right:
		texture.loadFromFile("Tileset/WoodSword_Right.png");
		position.x = position.x + (Global::minGridStep) + (Global::minGridStep / 2);
		position.y = position.y + (Global::minGridStep / 2);
		break;
	case Static::Direction::Down:
		texture.loadFromFile("Tileset/WoodSword_Down.png");
		position.x = position.x + (Global::minGridStep / 2);
		position.y = position.y + Global::minGridStep + (Global::minGridStep / 4);
		break;
	case Static::Direction::Up:
		texture.loadFromFile("Tileset/WoodSword_Up.png");
		position.x = position.x + (Global::minGridStep / 4);
		position.y = position.y - Global::minGridStep - (Global::minGridStep / 2);
		break;
	}
	sprite.setTexture(texture);
	width = sprite.getTextureRect().width;
	height = sprite.getTextureRect().height;
	sprite.setPosition(position.x, position.y);
}
void Sword::update(bool& isAttacking, bool& canAttack,std::vector<GameObject*>* worldMap, Animation* walkAnimation[3]){
	if (isAttacking){
		swordCurrentFrame++;
		if (isCollidingWithMonster(worldMap))
			updateMonster(worldMap);
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
bool Sword::isCollidingWithMonster(std::vector<GameObject*>* worldMap){
	collidingMonsterList.clear();
	bool isColliding = false;
	Point offset(0, 0);
	for each (GameObject* obj in *worldMap)
	{
			if (dynamic_cast<Monster*>(obj))
				if (intersect(fullMask, ((Monster*)obj)->fullMask, offset)){
					isColliding = true;
					collidingMonsterList.push_back(obj);
				}
	}
	return isColliding;
}
void Sword::updateMonster(std::vector<GameObject*>* worldMap){
	for each (GameObject* mstr in collidingMonsterList)
	{
		Monster* temp = (Monster*)mstr;
		temp->takeDamage(strength, worldMap, swordDir);
		//std::cout << "Collision at X:" << temp->xPosition << " Y:" << temp->yPosition<<std::endl;
	}
}
void Sword::endSword(){
	swordDelay = swordMaxDelay;
	swordCurrentFrame = swordMaxFrame;
}