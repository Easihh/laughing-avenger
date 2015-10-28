#ifndef PLAYER_BAR_H
#define PLAYER_BAR_H
#include "Item\Item.h"
#include "SFML\Graphics.hpp"
#include <sstream>
class PlayerBar{
public:
	PlayerBar();
	~PlayerBar();
	void setupPlayerBar();
	void setupMap();
	void setupPlayerMarker();
	void draw(sf::RenderWindow& mainWindow);
	void update();
	void setBarNextPosition(Point step);
	void decreaseCurrentHP(int amount);
	int getCurrentHP();
	void movePlayerBarToBottomScreen();
	void movePlayerBarToTopScreen();
	sf::RectangleShape playerMarker, playerBar, overworldMap;
	Point marker;
	sf::Sprite itemSlotS;
	int* diamondPtr;
	int* keysPtr;
	int* bombPtr;
private:
	Point bar, map, healthBarStart, itemSlotStart, diamondStart, itemSlotTextStart, bombStart, bombTextStart,
		diamondTextStart, keyTextStart, keyStart, itemSelection, swordSlot, itemSlotImage;
	sf::Texture fullHeartTexture,halfHeartTexture,emptyHeartTexture,itemSlotTexture,bombIconTexture,diamondIconTexture,
		keyIconTexture,woodSwordTexture;
	sf::Sprite sprite;
	int diamondAmount, keysAmount, bombAmount;
	void drawHearts(sf::RenderWindow& mainWindow);
	void drawPlayerBar(sf::RenderWindow& mainWindow);
	void drawItemsSlot(sf::RenderWindow& mainWindow);
	void drawBombInfo(sf::RenderWindow& mainWindow);
	void drawDiamondInfo(sf::RenderWindow& mainWindow);
	void drawKeyInfo(sf::RenderWindow& mainWindow);
	const int heartWidth = 16,heartHeight=16,maxHeartPerRow=8;
	void loadImages();
	sf::Font font;
	sf::Text txt;
	int currentHealthPoint, maxHealthPoint;
	std::stringstream ss;
};
#endif