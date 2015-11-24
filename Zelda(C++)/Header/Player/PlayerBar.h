#ifndef PLAYER_BAR_H
#define PLAYER_BAR_H
#include "Item\Item.h"
#include "SFML\Graphics.hpp"
#include <sstream>
#include "Type\SwordType.h"
class PlayerBar{
public:
	PlayerBar(Point pos);
	void setupPlayerBar();
	void setupMap();
	void setupPlayerMarker();
	void draw(sf::RenderWindow& mainWindow);
	void update();
	void setBarNextPosition(Point step);
	void decreaseCurrentHP(int amount);
	int getCurrentHP();
	void increaseBombAmount(int amount);
	void increaseRupeeAmount(int amount);
	bool isFullHP();
	SwordType mySword;
	void movePlayerBarToBottomScreen();
	void movePlayerBarToTopScreen();
	void updatePlayerMapMarker(Direction direction);
	sf::RectangleShape playerMarker, playerBar, overworldMap;
	Point marker;
	sf::Sprite itemSlotS;
	int diamondAmount, keysAmount, bombAmount;
	void increaseMaxHP();
private:
	Point bar, map, healthBarStart, itemSlotStart, diamondStart, itemSlotTextStart, bombStart, bombTextStart,
		diamondTextStart, keyTextStart, keyStart, itemSelection, swordSlot, itemSlotImage;
	sf::Texture fullHeartTexture,halfHeartTexture,emptyHeartTexture,itemSlotTexture,bombIconTexture,diamondIconTexture,
		keyIconTexture,woodSwordTexture;
	sf::Sprite sprite;
	void drawHearts(sf::RenderWindow& mainWindow);
	void drawPlayerBar(sf::RenderWindow& mainWindow);
	void drawItemsSlot(sf::RenderWindow& mainWindow);
	void drawBombInfo(sf::RenderWindow& mainWindow);
	void drawDiamondInfo(sf::RenderWindow& mainWindow);
	void drawKeyInfo(sf::RenderWindow& mainWindow);
	const int heartWidth = 16,heartHeight=16,maxHeartPerRow=8,maxRupee=999;
	void loadImages();
	sf::Font font;
	sf::Text txt;
	int currentHealthPoint, maxHealthPoint,maxBombAmount;
	std::stringstream ss;
};
#endif