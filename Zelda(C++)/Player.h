#ifndef PLAYER_H
#define PLAYER_H

#include "SFML\Graphics.hpp"
#include "GameObject.h"
#include "Animation.h"
#include "Sword.h"
#include "Monster.h"
#include "PlayerBar.h"
class Player:public GameObject{
public:
	Player(float x,float y);
	~Player();
	void update(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]);
	void draw(sf::RenderWindow& mainWindow);
	int worldX, worldY,healthPoint;
private:
	unsigned int stepToMove;
	Static::Direction dir;
	void completeMove();
	bool isColliding(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]);
	bool isCollidingWithMonster(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]);
	int xOffset, yOffset, stepToAlign, transitionStep,currentInvincibleFrame;
	int const maxTransitionStep = 90, maxInvincibleFrame=60;
	int getXOffset();
	int getYOffset();
	void loadImage();
	void takeDamage();
	void getUnalignedCount(Static::Direction nextDir);
	void snapToGrid();
	void drawPlayerBar(sf::RenderWindow& mainWindow);
	void drawText(sf::RenderWindow& mainWindow);
	sf::IntRect subRect;
	Animation* walkAnimation;
	Animation* attackAnimation;
	Sword* sword;
	void checkMapBoundaries();
	void screenTransition();
	bool canAttack,isAttacking,stepIsNegative,isScreenTransitioning,isInvincible;
	void checkInvincible();
	void pushback();
	void checkMovementInput(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]);
	void checkAttackInput();
	void checkInventoryInput();
	Monster* collidingMonster;
	PlayerBar* playerBar;
};
#endif