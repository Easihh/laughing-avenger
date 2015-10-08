#include "Inventory.h"
#include "Static.h"
#include <iostream>
Inventory::Inventory(){
	keyWasReleased = false;
	inventoryRect.setOutlineColor(sf::Color(64,0,128));
	inventoryRect.setOutlineThickness(3);
	inventoryRect.setFillColor(sf::Color::Transparent);
	sf::Vector2f size(Global::SCREEN_WIDTH/2, 96);
	inventoryRect.setSize(size);
	hasBoomrang = false;
	selectorInventoryXIndex = 0;
	selectorInventoryYIndex = 0;
	loadSelector();
}
void Inventory::loadSelector(){
	if (!texture.loadFromFile("Tileset/Selector.png"))
		std::cout << "Failed to load Selector";
	selector.setTexture(texture);
	selector.setPosition(228, 180);
}
Inventory::~Inventory(){}
void Inventory::transitionToInventory(PlayerBar* playerBar){
	playerBar->playerBar.setPosition(playerBar->playerBar.getPosition().x, playerBar->playerBar.getPosition().y + Global::SCREEN_HEIGHT - Global::inventoryHeight);
	playerBar->overworldMap.setPosition(playerBar->overworldMap.getPosition().x, playerBar->overworldMap.getPosition().y + Global::SCREEN_HEIGHT-Global::inventoryHeight);
	playerBar->playerMarker.setPosition(playerBar->playerMarker.getPosition().x, playerBar->playerMarker.getPosition().y + Global::SCREEN_HEIGHT - Global::inventoryHeight);
	inventoryRect.setPosition(playerBar->overworldMap.getPosition().x + 144, playerBar->overworldMap.getPosition().y -416);
	x = inventoryRect.getPosition().x;
	y = inventoryRect.getPosition().y;
	selector.setPosition(x,y);
	items[0][0]->sprite.setPosition(x, y);
	items[2][2]->sprite.setPosition(x + (2 * selectorWidth), y + (2*selectorHeight));
}
void Inventory::getInput(sf::Event& event){
	if (event.type == sf::Event::KeyReleased && event.key.code == sf::Keyboard::Q)
		keyWasReleased = true;
	if (event.type == sf::Event::KeyPressed && event.key.code == sf::Keyboard::Right){
		findNextSelectorPosition();
		selector.setPosition(x + (selectorInventoryXIndex *selectorWidth),y+  (selectorInventoryYIndex*selectorHeight));
	}
	if (event.type == sf::Event::KeyPressed && event.key.code == sf::Keyboard::Left){
		findNextSelectorPosition();
		selector.setPosition(x + (selectorInventoryXIndex *selectorWidth), y + (selectorInventoryYIndex*selectorHeight));
	}
}
void Inventory::findNextSelectorPosition(){
	bool found = false;
	for (int i = selectorInventoryXIndex; i < Static::inventoryRows; i++){
		for (int j = selectorInventoryYIndex; j < Static::inventoryCols; j++){
			if (items[i][j] != NULL && i != selectorInventoryXIndex && j != selectorInventoryYIndex){
				selectorInventoryYIndex = j;
				selectorInventoryXIndex = i;
				found = true;
			}
		}
	}
	if (!found){
		for (int i = 0; i < selectorInventoryXIndex; i++){
			for (int j = 0; j < selectorInventoryYIndex; j++){
				if (items[i][j] != NULL){
					selectorInventoryYIndex = j;
					selectorInventoryXIndex = i;
				}
			}
		}
	}
}
void Inventory::update(sf::Event& event, PlayerBar* playerBar){
	getInput(event);
	if (sf::Keyboard::isKeyPressed(sf::Keyboard::Q) && keyWasReleased)
		transitionBackToGame(playerBar);
}
void Inventory::transitionBackToGame(PlayerBar* playerBar){
	Static::gameState = Static::GameState::Playing;
	playerBar->playerBar.setPosition(playerBar->playerBar.getPosition().x, playerBar->playerBar.getPosition().y - Global::SCREEN_HEIGHT + Global::inventoryHeight);
	playerBar->overworldMap.setPosition(playerBar->overworldMap.getPosition().x, playerBar->overworldMap.getPosition().y - Global::SCREEN_HEIGHT + Global::inventoryHeight);
	playerBar->playerMarker.setPosition(playerBar->playerMarker.getPosition().x, playerBar->playerMarker.getPosition().y - Global::SCREEN_HEIGHT + Global::inventoryHeight);
	keyWasReleased = false;
}
void Inventory::drawInventoryItems(sf::RenderWindow& mainWindow){
	for (int i = 0; i < Static::inventoryRows; i++){
		for (int j = 0; j < Static::inventoryCols; j++){
			if (items[i][j] != NULL)
				mainWindow.draw(items[i][j]->sprite);
		}
	}
}
void Inventory::draw(sf::RenderWindow& mainWindow,PlayerBar* playerBar){
	mainWindow.setKeyRepeatEnabled(false);
	mainWindow.draw(inventoryRect);
	playerBar->draw(mainWindow);
	drawInventoryItems(mainWindow);
	mainWindow.draw(selector);
	mainWindow.display();
}