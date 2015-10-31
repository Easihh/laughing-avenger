#ifndef INVENTORY_H
#define INVENTORY_H
#include "SFML\Graphics.hpp"
#include "Player\PlayerBar.h"
#include "Item\Item.h"
class Inventory{
public:
	~Inventory();
	Inventory();
	void draw(sf::RenderWindow& mainWindow);
	void update(sf::Event& event);
	void transitionToInventory();
	void transitionBackToGame();
	bool keyWasReleased;
	Item* items[Static::inventoryRows][Static::inventoryCols];
	void updateInventoryPosition(Point step);
	int selectorInventoryXIndex, selectorInventoryYIndex;
	Item* getCurrentItem();
	void itemUse(Point position, Static::Direction dir, std::vector<GameObject*>* worldMap);
	void findNextSelectorPosition();
	PlayerBar* playerBar;
private:
	sf::RectangleShape inventoryRect, itemSelected;
	void getInput(sf::Event& event);
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