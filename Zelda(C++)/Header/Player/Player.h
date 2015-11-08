#ifndef PLAYER_H
#define PLAYER_H

#include "SFML\Graphics.hpp"
#include "Misc\GameObject.h"
#include "Misc\Animation.h"
#include "Player\Sword.h"
#include "Monster\Monster.h"
#include "Player\PlayerBar.h"
#include "Player\Inventory.h"
#include "Player\MovingSword.h"
class Player:public GameObject{
public:
	Player(Point position);
	~Player();
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void draw(sf::RenderWindow& mainWindow);
	int worldX, worldY,prevWorldX,prevWorldY;
	bool inventoryKeyReleased, attackKeyReleased, itemKeyReleased, isInsideShop, movePlayerToNewVector, movingSwordIsActive;
	std::unique_ptr<Inventory> inventory;
	std::unique_ptr<Point> pointBeforeTeleport;
	std::vector<std::unique_ptr<Animation>> walkingAnimation;
	std::vector<std::unique_ptr<Animation>> attackAnimation;
private:
	unsigned int stepToMove;
	Static::Direction dir;
	void completeMove();
	bool isColliding(std::vector<std::shared_ptr<GameObject>>* worldMap, std::unique_ptr<sf::RectangleShape>& mask, float xOffset, float yOffset);
	bool isCollidingWithMonster(std::vector<std::shared_ptr<GameObject>>* worldMap);
	int xOffset, yOffset, stepToAlign, transitionStep,currentInvincibleFrame;
	int const maxTransitionStep = 90, maxInvincibleFrame=60;
	int getXOffset();
	int getYOffset();
	void loadImage();
	void takeDamage(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void getUnalignedCount(Static::Direction nextDir);
	void snapToGrid();
	void drawPlayerBar(sf::RenderWindow& mainWindow);
	void drawText(sf::RenderWindow& mainWindow);
	sf::IntRect subRect;
	int walkAnimationIndex;
	int attackAnimationIndex;
	std::unique_ptr<Sword> sword;
	std::shared_ptr<MovingSword> movingSword;
	void endScreenTransition();
	void screenTransition();
	bool canAttack,isAttacking,stepIsNegative,isScreenTransitioning,isInvincible;
	void checkInvincible();
	void pushback(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void checkMovementInput(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void checkAttackInput();
	void checkInventoryInput();
	void checkItemUseInput(std::vector<std::shared_ptr<GameObject>>* worldMap);
	bool isOutsideMapBound(Point pos);
	std::shared_ptr<GameObject> collidingMonster;
	const int textSize = 12;
};
#endif