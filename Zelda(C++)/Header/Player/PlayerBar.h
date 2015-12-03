#ifndef PLAYER_BAR_H
#define PLAYER_BAR_H
#include "Item\Item.h"
#include "SFML\Graphics.hpp"
#include <sstream>
#include "Type\SwordType.h"
#include "Type\DungeonLevel.h"
class PlayerBar{
public:
	PlayerBar(Point pos);
	void setupPlayerBar();
	void setupMap();
	void setupPlayerMarker();
	void draw(sf::RenderWindow& mainWindow);
	void update();
	void setBarNextPosition(Point step);
	void setPlayerBar(Point pt);
	void decreaseCurrentHP(int amount);
	int getCurrentHP();
	void increaseBombAmount(int amount);
	void increaseRupeeAmount(int amount);
	bool isFullHP();
	SwordType mySword;
	DungeonLevel currentDungeon;
	void movePlayerBarToBottomScreen();
	void movePlayerBarToTopScreen();
	void updatePlayerMapMarker(Direction direction);
	sf::RectangleShape playerMarker,playerBar, overworldMap,dungeonMap,dungeonPlayerMarker,dungeonBossMarker;
	Point marker,dungeonMarker,bossMarker;
	sf::Sprite itemSlotS;
	int diamondAmount, keysAmount, bombAmount;
	void increaseMaxHP();
	void resetDungeonPlayerMarker();
	bool hasDungeonMap,hasDungeonCompass;
	void increaseKeyAmount();
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
	void drawDungeonMap(sf::RenderWindow& mainWindow);
	const int heartWidth = 16,heartHeight=16,maxHeartPerRow=8,maxRupee=999;
	void loadImages();
	sf::Font font;
	sf::Text txt;
	int currentHealthPoint, maxHealthPoint,maxBombAmount;
	std::stringstream ss;
};
#endif