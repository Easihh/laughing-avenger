#ifndef INVENTORY_H
#define INVENTORY_H
#include "SFML\Graphics.hpp"
#include "Player\PlayerBar.h"
#include "Item\Item.h"
class Inventory{
public:
	Inventory(int worldX, int worldY);
	void draw(sf::RenderWindow& mainWindow);
	void update(sf::Event& event);
	void transitionToInventory();
	void transitionBackToGame();
	bool keyWasReleased;
	std::vector<std::unique_ptr<Item>> items;
	void updateInventoryPosition(Point step);
	int selectorInventoryIndex;
	Item* getCurrentItem();
	void acquireBow();
	void itemUse(Point position,Direction dir, std::vector<std::shared_ptr<GameObject>>* worldMap);
	void findNextSelectorPositionRight();
	void findNextSelectorPositionLeft();
	std::unique_ptr<PlayerBar> playerBar;
	void setInventoryPosition(Point pos);
	bool hasDungeon1Triforce, hasDungeon2Triforce, hasDungeon3Triforce, hasDungeon4Triforce, hasDungeon5Triforce
		, hasDungeon6Triforce, hasDungeon7Triforce, hasDungeon8Triforce;
private:
	bool isPossessingBow(Item* item, int index);
	sf::RectangleShape inventoryRect, itemSelected;
	void getInput(sf::Event& event);
	Point startPosition;
	sf::Sprite selector;
	sf::Font font;
	sf::Text txt;
	sf::Texture selectorTexture,completeBowTexture;
	const int selectorWidth=32, selectorHeight = 32;
	void loadSelector();
	void drawInventoryItems(sf::RenderWindow& mainWindow);
	void drawInventoryText(sf::RenderWindow& mainWindow);
	bool hasBoomrang,hasBomb;
	void loadInventoryRectangle();
	void loadInventoryCurrentSelection();
	void selectInventoryItem();
	Point inventoryRectPt, itemSelectedPt, inventoryText,itemUseButtonText,triforce;
	sf::Sprite selectedItem;
};
#endif