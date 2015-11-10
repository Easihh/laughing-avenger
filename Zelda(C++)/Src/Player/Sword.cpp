#include "Player\Sword.h"
#include <string>
#include "Monster\Monster.h"
#include <iostream>
Sword::Sword(Point pos,Direction dir){
	depth = 0;
	position = pos;
	swordCurrentFrame = 0;
	swordDelay = 0;
	swordDir = dir;
	loadImage(dir);
	setupFullMask();
	strength = 1;
}
Sword::~Sword(){}
void Sword::loadImage(Direction dir){
	switch (dir){
	case Direction::Left:
		texture.loadFromFile("Tileset/WoodSword_Left.png");
		position.x = position.x - (Global::minGridStep) - (Global::minGridStep / 2);
		position.y = position.y + (Global::minGridStep / 2);
		break;
	case Direction::Right:
		texture.loadFromFile("Tileset/WoodSword_Right.png");
		position.x = position.x + (Global::minGridStep) + (Global::minGridStep / 2);
		position.y = position.y + (Global::minGridStep / 2);
		break;
	case Direction::Down:
		texture.loadFromFile("Tileset/WoodSword_Down.png");
		position.x = position.x + (Global::minGridStep / 2);
		position.y = position.y + Global::minGridStep + (Global::minGridStep / 4);
		break;
	case Direction::Up:
		texture.loadFromFile("Tileset/WoodSword_Up.png");
		position.x = position.x + (Global::minGridStep / 2);
		position.y = position.y - Global::minGridStep - (Global::minGridStep / 2);
		break;
	}
	sprite.setTexture(texture);
	width = sprite.getTextureRect().width;
	height = sprite.getTextureRect().height;
	sprite.setPosition(position.x, position.y);
}
void Sword::update(bool& isAttacking, bool& canAttack, std::vector<std::shared_ptr<GameObject>>* worldMap, std::vector<std::unique_ptr<Animation>>* walkAnimation) {
	if (isAttacking){
		swordCurrentFrame++;
		if (isCollidingWithMonster(worldMap))
			updateMonster(worldMap);
		if (swordCurrentFrame >= swordMaxFrame){
			isAttacking = false;
			swordCurrentFrame = 0;
			//reset the attack frame to standing ;used when player attack during a non-standing frame
			for(auto& obj : *walkAnimation){
				obj->reset();
				obj->setSubRectangle(swordDir);
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
bool Sword::isCollidingWithMonster(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	collidingMonsterList.clear();
	bool isColliding = false;
	Point offset(0, 0);
	for(auto& obj:*worldMap)
	{
		if (dynamic_cast<Monster*>(obj.get()))
			if (intersect(fullMask, ((Monster*)obj.get())->fullMask, offset)){
				isColliding = true;
				collidingMonsterList.push_back(obj);
		}
	}
	return isColliding;
}
void Sword::updateMonster(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	for(auto& mstr : collidingMonsterList)
	{
		Monster* temp = (Monster*)mstr.get();
		temp->takeDamage(strength, worldMap,swordDir);
		//std::cout << "Collision at X:" << temp->xPosition << " Y:" << temp->yPosition<<std::endl;
	}
}
void Sword::endSword(){
	swordDelay = swordMaxDelay;
	swordCurrentFrame = swordMaxFrame;
}