#ifndef INVENTORY_H
#define INVENTORY_H
#include "SFML\Graphics.hpp"
#include "Player\PlayerBar.h"
#include "Item\Item.h"
class Inventory{
public:
	~Inventory();
	Inventory();
	void draw(sf::RenderWindow& mainWindow,PlayerBar* playerBar);
	void update(sf::Event& event, PlayerBar* playerBar);
	void transitionToInventory(PlayerBar* playerBar);
	void transitionBackToGame(PlayerBar* playerBar);
	bool keyWasReleased;
	Item* items[Static::inventoryRows][Static::inventoryCols];
	void updateInventoryPosition(float stepX, float stepY);
	int selectorInventoryXIndex, selectorInventoryYIndex;
	Item* getCurrentItem();
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
	bool hasBoomrang;
	void findNextSelectorPosition();
	void loadInventoryRectangle();
	void loadInventoryCurrentSelection();
	void selectFirstInventoryItemOwned();
	float x, y, itemSelectedX, itemSelectedY, inventoryTextX, inventoryTextY,
		itemUseButtonTextX, itemUseButtonTextY;
	sf::Sprite selectedItem;
};
#endif