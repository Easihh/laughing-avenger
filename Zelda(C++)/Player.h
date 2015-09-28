#ifndef PLAYER_H
#define PLAYER_H

#include "SFML\Graphics.hpp"
#include "GameObject.h"
#include "Animation.h"
#include "Sword.h"
class Player:public GameObject{
public:
	Player(float x,float y);
	~Player();
	void update(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]);
	void draw(sf::RenderWindow& mainWindow);
private:
	unsigned int stepToMove;
	Static::Direction dir;
	void completeMove();
	bool isColliding(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]);
	int xOffset, yOffset,swordMaxFrame,swordCurrentFrame,swordDelay,swordMaxDelay,stepToAlign;
	int getXOffset();
	int getYOffset();
	void loadImage();
	void getUnalignedCount(Static::Direction nextDir);
	void snapToGrid();
	void drawText(sf::RenderWindow& mainWindow);
	sf::IntRect subRect;
	Animation* walkAnimation;
	Animation* attackAnimation;
	Sword* sword;
	bool canAttack,isAttacking,stepIsNegative;
};
#endif