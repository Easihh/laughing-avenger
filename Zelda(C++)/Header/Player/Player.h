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
#include "Type\Layer.h"
class Player:public GameObject{
public:
	Player(Point position);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void draw(sf::RenderWindow& mainWindow);
	int worldX, worldY,prevWorldX,prevWorldY;
	bool inventoryKeyReleased, attackKeyReleased, itemKeyReleased, movePlayerToNewVector, 
		movingSwordIsActive, isObtainingItem, boomerangIsActive;
	std::unique_ptr<Inventory> inventory;
	std::unique_ptr<Point> pointBeforeTeleport;
	std::vector<std::unique_ptr<Animation>> walkingAnimation;
	std::vector<std::unique_ptr<Animation>> attackAnimation;
	Layer currentLayer,prevLayer;
private:
	unsigned int stepToMove;
	void completeMove();
	bool isColliding(std::vector<std::shared_ptr<GameObject>>* worldMap, std::unique_ptr<sf::RectangleShape>& mask, float xOffset, float yOffset);
	int xOffset, yOffset, stepToAlign, transitionStep,currentInvincibleFrame;
	int const maxTransitionStep = 90, maxInvincibleFrame=60;
	int getXOffset();
	int getYOffset();
	void loadImage();
	void takeDamage(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void getUnalignedCount(Direction nextDir);
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
	void playerPushbackUpdate();
	void playerPushBack(std::vector<std::shared_ptr<GameObject>>* worldMap,Point monsterPosition);
	void checkMovementInput(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void checkAttackInput();
	void checkInventoryInput();
	void checkItemUseInput(std::vector<std::shared_ptr<GameObject>>* worldMap);
	const int textSize = 12;
};
#endif