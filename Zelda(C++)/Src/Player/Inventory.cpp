#include "Player\Inventory.h"
#include "Utility\Static.h"
#include <iostream>
Inventory::Inventory(){
	keyWasReleased = hasBoomrang=false;
	hasBomb = true;
	inventoryText.setPoint(52,52);
	itemUseButtonText.setPoint(16, 136);
	font.loadFromFile("zelda.ttf");
	playerBar = std::make_unique<PlayerBar>();
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
	//selectorInventoryXIndex = -1;
	//selectorInventoryYIndex = -1;
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
Item* Inventory::getCurrentItem() {
	return items[selectorInventoryIndex].get();
}
void Inventory::itemUse(Point position,Direction dir, std::vector<std::shared_ptr<GameObject>>* worldMap) {
	int i = selectorInventoryIndex;
	if (getCurrentItem()!=NULL){
		PlayerInfo info(position, playerBar->bombPtr, playerBar->diamondPtr, playerBar->keysPtr, dir);
		items[i]->onUse(info, worldMap);
		if (!items[i]->isActive){
			items[i] = NULL;
			findNextSelectorPosition();
			playerBar->itemSlotS = getCurrentItem()->sprite;
		}
	}
}
void Inventory::transitionToInventory(){
	playerBar->movePlayerBarToBottomScreen();
	inventoryRect.setPosition(inventoryRectPt.x, inventoryRectPt.y);
	itemSelected.setPosition(itemSelectedPt.x, itemSelectedPt.y);
	int maxItemPerRow = Static::inventoryCols;
	int row = 0;
	for (int i = 0; i < items.size(); i++){
		if(items[i] != NULL){
			row = i/maxItemPerRow;
			items[i]->sprite.setPosition(inventoryRectPt.x + (i*selectorWidth), inventoryRectPt.y + (row*selectorWidth));
		}
	}
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
	if (event.type == sf::Event::KeyPressed && (event.key.code == sf::Keyboard::Right || 
		event.key.code==sf::Keyboard::Left)){
		findNextSelectorPosition();
	}
}
void Inventory::findNextSelectorPosition(){
	bool found = false;
	for (int i = selectorInventoryIndex; i < items.size(); i++){
		if (items[i] != NULL && i!=selectorInventoryIndex && !found){
			if(Static::gameState == Static::Inventory)
				Sound::playSound(SoundType::Selector);
			selectorInventoryIndex = i;
			found = true;
			selectedItem = items[i]->sprite;
			selectedItem.setPosition(itemSelectedPt.x, itemSelectedPt.y);
			selector.setPosition(items[i]->sprite.getPosition().x,items[i]->sprite.getPosition().y);
		}
	}
	if (!found){
		for (int i = 0; i < selectorInventoryIndex; i++){
			if (items[i] != NULL){
				if(Static::gameState==Static::Inventory)
					Sound::playSound(SoundType::Selector);
				selectorInventoryIndex = i;
				selectedItem = items[i]->sprite;
				selectedItem.setPosition(itemSelectedPt.x, itemSelectedPt.y);
				selector.setPosition(items[i]->sprite.getPosition().x,items[i]->sprite.getPosition().y);
			}
		}
	}
}
void Inventory::update(sf::Event& event){
	getInput(event);
	if (sf::Keyboard::isKeyPressed(sf::Keyboard::Q) && keyWasReleased)
		transitionBackToGame();
}
void Inventory::transitionBackToGame(){
	Static::gameState = Static::GameState::Playing;
	playerBar->movePlayerBarToTopScreen();
	playerBar->itemSlotS=selectedItem;
	keyWasReleased = false;
}
void Inventory::drawInventoryItems(sf::RenderWindow& mainWindow){
	mainWindow.draw(selectedItem);
	for (int i = 0; i < items.size(); i++){
		if (items[i] != NULL)
			mainWindow.draw(items[i]->sprite);
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
void Inventory::draw(sf::RenderWindow& mainWindow){
	mainWindow.setKeyRepeatEnabled(false);
	mainWindow.draw(inventoryRect);
	playerBar->draw(mainWindow);
	drawInventoryItems(mainWindow);
	mainWindow.draw(itemSelected);
	mainWindow.draw(selector);
	drawInventoryText(mainWindow);
	mainWindow.display();
}