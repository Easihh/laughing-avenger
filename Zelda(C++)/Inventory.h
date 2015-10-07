#ifndef INVENTORY_H
#define INVENTORY_H
#include "SFML\Graphics.hpp"
#include "PlayerBar.h"
class Inventory{
public:
	~Inventory();
	Inventory();
	void draw(sf::RenderWindow& mainWindow,PlayerBar* playerBar);
	void update(sf::Event& event, PlayerBar* playerBar);
	void transitionToInventory(PlayerBar* playerBar);
	void transitionBackToGame(PlayerBar* playerBar);
	bool keyWasReleased;
private:
	sf::RectangleShape inventoryRect;
	void getInput(sf::Event& event);
};
#endif