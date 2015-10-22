#ifndef PLAYER_H
#define PLAYER_H

#include "SFML\Graphics.hpp"
#include "Misc\GameObject.h"
#include "Misc\Animation.h"
#include "Player\Sword.h"
#include "Monster\Monster.h"
#include "Player\PlayerBar.h"
#include "Player\Inventory.h"
class Player:public GameObject{
public:
	Player(Point position);
	~Player();
	void update(std::vector<GameObject*>* worldMap);
	void draw(sf::RenderWindow& mainWindow);
	int worldX, worldY;
	bool inventoryKeyReleased, attackKeyReleased, itemKeyReleased;
	Inventory* inventory;
	PlayerBar* playerBar;
private:
	unsigned int stepToMove;
	Static::Direction dir;
	void completeMove();
	bool isColliding(const std::vector<GameObject*>* worldLayer, sf::RectangleShape* mask, float xOffset, float yOffset);
	bool isCollidingWithMonster(const std::vector<GameObject*>* worldLayer);
	int xOffset, yOffset, stepToAlign, transitionStep,currentInvincibleFrame;
	int const maxTransitionStep = 90, maxInvincibleFrame=60;
	int getXOffset();
	int getYOffset();
	void loadImage();
	void takeDamage(const std::vector<GameObject*>* worldLayer);
	void getUnalignedCount(Static::Direction nextDir);
	void snapToGrid();
	void drawPlayerBar(sf::RenderWindow& mainWindow);
	void drawText(sf::RenderWindow& mainWindow);
	sf::IntRect subRect;
	int walkAnimationIndex;
	Animation* attackAnimation[3];
	int attackAnimationIndex;
	Sword* sword;
	void endScreenTransition();
	void screenTransition();
	bool canAttack,isAttacking,stepIsNegative,isScreenTransitioning,isInvincible;
	void checkInvincible();
	void pushback(const std::vector<GameObject*>* worldLayer);
	void checkMovementInput(const std::vector<GameObject*>* worldLayer);
	void checkAttackInput();
	void checkInventoryInput();
	void checkItemUseInput(std::vector<GameObject*>* worldMap);
	bool isOutsideMapBound(float x, float y);
	Monster* collidingMonster;
	Animation * walkingAnimation[3];
};
#endif