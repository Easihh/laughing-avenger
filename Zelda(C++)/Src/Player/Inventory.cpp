#include "Player\Inventory.h"
#include "Utility\Static.h"
#include <iostream>
Inventory::Inventory(){
	keyWasReleased = false;
	hasBoomrang = false;
	inventoryTextX = 52;
	inventoryTextY = 52;
	itemUseButtonTextX = 16;
	itemUseButtonTextY = 136;
	font.loadFromFile("zelda.ttf");
	txt.setFont(font);
	loadInventoryCurrentSelection();
	loadInventoryRectangle();
	loadSelector();
}
void Inventory::loadInventoryCurrentSelection(){
	itemSelectedX = 100;
	itemSelectedY = 100;
	itemSelected.setOutlineColor(sf::Color(64, 0, 128));
	itemSelected.setOutlineThickness(3);
	itemSelected.setFillColor(sf::Color::Transparent);
	sf::Vector2f size(Global::TileWidth, Global::TileHeight);
	itemSelected.setSize(size);
}
void Inventory::loadInventoryRectangle(){
	x = 192;
	y = 96;
	inventoryRect.setOutlineColor(sf::Color(64, 0, 128));
	inventoryRect.setOutlineThickness(3);
	inventoryRect.setFillColor(sf::Color::Transparent);
	sf::Vector2f size(Global::SCREEN_WIDTH / 2, 96);
	inventoryRect.setSize(size);
}
void Inventory::loadSelector(){
	selectorInventoryXIndex = -1;
	selectorInventoryYIndex = -1;
	if (!texture.loadFromFile("Tileset/Selector.png"))
		std::cout << "Failed to load Selector";
	selector.setTexture(texture);
	selector.setPosition(228, 180);
}
Inventory::~Inventory(){}
void Inventory::updateInventoryPosition(float stepX,float stepY){
	x += stepX;
	y += stepY;
	itemSelectedX += stepX;
	itemSelectedY += stepY;
	inventoryTextX += stepX;
	inventoryTextY += stepY;
	itemUseButtonTextX += stepX;
	itemUseButtonTextY += stepY;
}
Item* Inventory::getCurrentItem(){
	return items[selectorInventoryXIndex][selectorInventoryYIndex];
}
void Inventory::transitionToInventory(PlayerBar* playerBar){
	playerBar->movePlayerBarToBottomScreen();
	inventoryRect.setPosition(x, y);
	itemSelected.setPosition(itemSelectedX, itemSelectedY);
	selector.setPosition(x,y);
	selectFirstInventoryItemOwned();
	items[0][0]->sprite.setPosition(x, y);
	items[2][2]->sprite.setPosition(x + (2 * selectorWidth), y + (2*selectorHeight));
}
void Inventory::selectFirstInventoryItemOwned(){
	for (int i = 0; i < Static::inventoryRows; i++){
		for (int j = 0; j < Static::inventoryCols; j++){
			if (items[i][j] != NULL){
				selectorInventoryYIndex = j;
				selectorInventoryXIndex = i;
				selectedItem = items[i][j]->sprite;
				selectedItem.setPosition(itemSelectedX, itemSelectedY);
				return;
			}
		}
	}
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
				selectedItem = items[i][j]->sprite;
				selectedItem.setPosition(itemSelectedX, itemSelectedY);
			}
		}
	}
	if (!found){
		for (int i = 0; i < selectorInventoryXIndex; i++){
			for (int j = 0; j < selectorInventoryYIndex; j++){
				if (items[i][j] != NULL){
					selectorInventoryYIndex = j;
					selectorInventoryXIndex = i;
					selectedItem = items[i][j]->sprite;
					selectedItem.setPosition(itemSelectedX, itemSelectedY);
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
	playerBar->movePlayerBarToTopScreen();
	playerBar->itemSlotS=selectedItem;
	keyWasReleased = false;
}
void Inventory::drawInventoryItems(sf::RenderWindow& mainWindow){
	mainWindow.draw(selectedItem);
	for (int i = 0; i < Static::inventoryRows; i++){
		for (int j = 0; j < Static::inventoryCols; j++){
			if (items[i][j] != NULL)
				mainWindow.draw(items[i][j]->sprite);
		}
	}
}
void Inventory::drawInventoryText(sf::RenderWindow& mainWindow){
	txt.setCharacterSize(14);
	txt.setColor(sf::Color::Red);
	txt.setPosition(inventoryTextX, inventoryTextY);
	txt.setString("INVENTORY");
	mainWindow.draw(txt);

	txt.setColor(sf::Color::White);
	txt.setPosition(itemUseButtonTextX, itemUseButtonTextY);
	txt.setString("USE S BUTTON\n FOR THIS");
	mainWindow.draw(txt);
}
void Inventory::draw(sf::RenderWindow& mainWindow,PlayerBar* playerBar){
	mainWindow.setKeyRepeatEnabled(false);
	mainWindow.draw(inventoryRect);
	playerBar->draw(mainWindow);
	drawInventoryItems(mainWindow);
	mainWindow.draw(itemSelected);
	mainWindow.draw(selector);
	drawInventoryText(mainWindow);
	mainWindow.display();
}