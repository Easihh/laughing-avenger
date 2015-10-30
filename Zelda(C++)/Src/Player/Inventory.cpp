#include "Player\Inventory.h"
#include "Utility\Static.h"
#include <iostream>
Inventory::Inventory(){
	keyWasReleased = hasBoomrang = false;
	inventoryText.setPoint(52,52);
	itemUseButtonText.setPoint(16, 136);
	font.loadFromFile("zelda.ttf");
	txt.setFont(font);
	loadInventoryCurrentSelection();
	loadInventoryRectangle();
	loadSelector();
}
void Inventory::loadInventoryCurrentSelection(){
	itemSelectedPt.setPoint(100,100);
	itemSelected.setOutlineColor(sf::Color(64, 0, 128));
	itemSelected.setOutlineThickness(3);
	itemSelected.setFillColor(sf::Color::Transparent);
	sf::Vector2f size(Global::TileWidth, Global::TileHeight);
	itemSelected.setSize(size);
}
void Inventory::loadInventoryRectangle(){
	inventoryRectPt.setPoint(192,96);
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
void Inventory::updateInventoryPosition(Point step){
	inventoryRectPt+=(step);
	itemSelectedPt+=(step);
	inventoryText+=(step);
	itemUseButtonText+=(step);
}
Item* Inventory::getCurrentItem(){
	return items[selectorInventoryXIndex][selectorInventoryYIndex];
}
void Inventory::transitionToInventory(PlayerBar* playerBar){
	playerBar->movePlayerBarToBottomScreen();
	inventoryRect.setPosition(inventoryRectPt.x, inventoryRectPt.y);
	itemSelected.setPosition(itemSelectedPt.x, itemSelectedPt.y);
	items[0][0]->sprite.setPosition(inventoryRectPt.x, inventoryRectPt.y);
	items[2][2]->sprite.setPosition(inventoryRectPt.x + (2 * selectorWidth), inventoryRectPt.y + (2 * selectorHeight));
	selectInventoryItem();
}
void Inventory::selectInventoryItem(){
	Item* current = getCurrentItem();
	if (current != NULL)
		selector.setPosition(current->sprite.getPosition().x, current->sprite.getPosition().y);
	else selector.setPosition(inventoryRectPt.x, inventoryRectPt.y);
}
void Inventory::getInput(sf::Event& event){
	if (event.type == sf::Event::KeyReleased && event.key.code == sf::Keyboard::Q)
		keyWasReleased = true;
	if (event.type == sf::Event::KeyPressed && event.key.code == sf::Keyboard::Right){
		findNextSelectorPosition();
		selector.setPosition(inventoryRectPt.x + (selectorInventoryXIndex *selectorWidth), inventoryRectPt.y + (selectorInventoryYIndex*selectorHeight));
	}
	if (event.type == sf::Event::KeyPressed && event.key.code == sf::Keyboard::Left){
		findNextSelectorPosition();
		selector.setPosition(inventoryRectPt.x + (selectorInventoryXIndex *selectorWidth), inventoryRectPt.y + (selectorInventoryYIndex*selectorHeight));
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
				selectedItem.setPosition(itemSelectedPt.x, itemSelectedPt.y);
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
					selectedItem.setPosition(itemSelectedPt.x, itemSelectedPt.y);
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
	txt.setPosition(inventoryText.x, inventoryText.y);
	txt.setString("INVENTORY");
	mainWindow.draw(txt);

	txt.setColor(sf::Color::White);
	txt.setPosition(itemUseButtonText.x, itemUseButtonText.y);
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