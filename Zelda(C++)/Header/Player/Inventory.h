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
	void itemUse(Point position,Direction dir, std::vector<std::shared_ptr<GameObject>>* worldMap);
	void findNextSelectorPositionRight();
	void findNextSelectorPositionLeft();
	std::unique_ptr<PlayerBar> playerBar;
	void setInventoryPosition(Point pos);
private:
	sf::RectangleShape inventoryRect, itemSelected;
	void getInput(sf::Event& event);
	Point startPosition;
	sf::Sprite selector;
	sf::Font font;
	sf::Text txt;
	sf::Texture texture;
	const int selectorWidth=32, selectorHeight = 32;
	void loadSelector();
	void drawInventoryItems(sf::RenderWindow& mainWindow);
	void drawInventoryText(sf::RenderWindow& mainWindow);
	bool hasBoomrang,hasBomb;
	void loadInventoryRectangle();
	void loadInventoryCurrentSelection();
	void selectInventoryItem();
	Point inventoryRectPt, itemSelectedPt, inventoryText,itemUseButtonText;
	sf::Sprite selectedItem;
};
#endif