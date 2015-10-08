#ifndef INVENTORY_H
#define INVENTORY_H
#include "SFML\Graphics.hpp"
#include "PlayerBar.h"
#include "Item.h"
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
private:
	sf::RectangleShape inventoryRect;
	void getInput(sf::Event& event);
	sf::Sprite selector;
	sf::Texture texture;
	const int selectorWidth=32, selectorHeight = 32;
	void loadSelector();
	void drawInventoryItems(sf::RenderWindow& mainWindow);
	bool hasBoomrang;
	void findNextSelectorPosition();
	int selectorInventoryXIndex, selectorInventoryYIndex;
	float x, y;
};
#endif