#include "Player\Inventory.h"
#include "Utility\Static.h"
#include "Item\Arrow.h"
#include <iostream>
Inventory::Inventory(int worldX,int worldY){
	float startX = worldY*Global::roomHeight;
	float startY = worldX*Global::roomWidth;
	startPosition = Point(startX,startY);
	keyWasReleased = hasBoomrang=false;
	hasDungeon1Triforce = hasDungeon2Triforce = hasDungeon3Triforce = hasDungeon4Triforce = hasDungeon5Triforce = hasDungeon6Triforce,
		hasDungeon7Triforce = hasDungeon8Triforce = false;
	hasBomb = true;
	inventoryText.setPoint(startPosition.x + 52, startPosition.y+52);
	itemUseButtonText.setPoint(startPosition.x + 16, startPosition.y+136);
	triforce.setPoint(startPosition.x + 250, startPosition.y + 350);
	font.loadFromFile("zelda.ttf");
	playerBar = std::make_unique<PlayerBar>(startPosition);
	txt.setFont(font);
	loadInventoryCurrentSelection();
	loadInventoryRectangle();
	loadSelector();
}
void Inventory::loadInventoryCurrentSelection(){
	itemSelectedPt.setPoint(startPosition.x + 100, startPosition.y+100);
	itemSelected.setOutlineColor(sf::Color(64, 0, 128));
	itemSelected.setOutlineThickness(3);
	itemSelected.setFillColor(sf::Color::Transparent);
	sf::Vector2f size(Global::TileWidth, Global::TileHeight);
	itemSelected.setSize(size);
}
void Inventory::loadInventoryRectangle(){
	inventoryRectPt.setPoint(startPosition.x + 192, startPosition.y+96);
	inventoryRect.setOutlineColor(sf::Color(64, 0, 128));
	inventoryRect.setOutlineThickness(3);
	inventoryRect.setFillColor(sf::Color::Transparent);
	sf::Vector2f size(Global::SCREEN_WIDTH / 2, 96);
	inventoryRect.setSize(size);
}
void Inventory::loadSelector(){
	//selectorInventoryXIndex = -1;
	//selectorInventoryYIndex = -1;
	if (!selectorTexture.loadFromFile("Tileset/Selector.png"))
		std::cout << "Failed to load Selector";
	selector.setTexture(selectorTexture);
	selector.setPosition(startPosition.x + 228, startPosition.y+180);
}
void Inventory::updateInventoryPosition(Point step){
	inventoryRectPt+=(step);
	itemSelectedPt+=(step);
	inventoryText+=(step);
	itemUseButtonText+=(step);
	triforce += (step);
}
void Inventory::setInventoryPosition(Point newPt){
	inventoryRectPt.setPoint(newPt.x + 192, newPt.y + 96);
	itemSelectedPt.setPoint(newPt.x + 100, newPt.y + 100);
	inventoryText.setPoint(newPt.x + 52, newPt.y + 52);
	itemUseButtonText.setPoint(newPt.x + 16, newPt.y + 136);
	triforce.setPoint(newPt.x + 250, newPt.y + 350);
}
Item* Inventory::getCurrentItem() {
	return items[selectorInventoryIndex].get();
}
void Inventory::itemUse(Point pos,Direction dir, std::vector<std::shared_ptr<GameObject>>* worldMap) {
	int i = selectorInventoryIndex;
	if (getCurrentItem()!=NULL){
		items[i]->onUse(pos, worldMap,dir);
		if (!items[i]->isActive){
			items[i] = NULL;
			findNextSelectorPositionRight();
			playerBar->itemSlotS = getCurrentItem()->sprite;
		}
	}
}
void Inventory::acquireBow(){
	Arrow* temp = (Arrow*)items.at(2).get();
	completeBowTexture.loadFromFile("Tileset/BowAndArrow.png");
	temp->texture = completeBowTexture;
	temp->sprite.setTexture(completeBowTexture);
	temp->bowIsActive = true;
}
void Inventory::transitionToInventory(){
	playerBar->movePlayerBarToBottomScreen();
	inventoryRect.setPosition(inventoryRectPt.x, inventoryRectPt.y);
	itemSelected.setPosition(itemSelectedPt.x, itemSelectedPt.y);
	int maxItemPerRow = Static::inventoryCols;
	int row = 0;
	int col = 0;
	for (int i = 0; i < items.size(); i++){
		if(items[i] != NULL){
			row = i/maxItemPerRow;
			col = i%maxItemPerRow;
			items[i]->sprite.setPosition(inventoryRectPt.x + (col*selectorWidth), inventoryRectPt.y + (row*selectorWidth));
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
	if (event.type == sf::Event::KeyPressed && (event.key.code == sf::Keyboard::Right))
		findNextSelectorPositionRight();
	if(event.type == sf::Event::KeyPressed && (event.key.code == sf::Keyboard::Left))
		findNextSelectorPositionLeft();
}
void Inventory::findNextSelectorPositionLeft() {
	bool found = false;
	for(int i = selectorInventoryIndex; i >= 0; i--){
		if (items[i] != NULL && i != selectorInventoryIndex && !found && items[i]->isActive){
			if (!dynamic_cast<Arrow*>(items[i].get())){
				selectorInventoryIndex = i;
				found = true;
			}
			//pointing at arrow item; check if possessing the bow to allow selection.
			else found = isPossessingBow(items[i].get(), i);
		}
	}
	if(!found){
		for(int i = items.size()-1; i > selectorInventoryIndex; i--)
			if (items[i] != NULL && items[i]->isActive){
				if (!dynamic_cast<Arrow*>(items[i].get())){
					selectorInventoryIndex = i;
					found = true;
				}
				//pointing at arrow item; check if possessing the bow to allow selection.
				else found = isPossessingBow(items[i].get(), i);
			}
	}
	if(found){
		if(Static::gameState == GameState::InventoryMenu)
			Sound::playSound(GameSound::SoundType::Selector);
		int i = selectorInventoryIndex;
		selectedItem = items[i]->sprite;
		selectedItem.setPosition(itemSelectedPt.x, itemSelectedPt.y);
		selector.setPosition(items[i]->sprite.getPosition().x, items[i]->sprite.getPosition().y);
	}
}
void Inventory::findNextSelectorPositionRight(){
	bool found = false;
	for (int i = selectorInventoryIndex; i < items.size(); i++){
		if (items[i] != NULL && i!=selectorInventoryIndex && !found && items[i]->isActive){
			if (!dynamic_cast<Arrow*>(items[i].get())){
				selectorInventoryIndex = i;
				found = true;
			}
			//pointing at arrow item; check if possessing the bow to allow selection.
			else found = isPossessingBow(items[i].get(), i);
		}
	}
	if(!found){
		for(int i = 0; i < selectorInventoryIndex; i++)
			if (items[i] != NULL &&items[i]->isActive){
				if (!dynamic_cast<Arrow*>(items[i].get())){
					selectorInventoryIndex = i;
					found = true;
				}
				//pointing at arrow item; check if possessing the bow to allow selection.
				else found = isPossessingBow(items[i].get(), i);
			}
	}
	if(found){
		if (Static::gameState == GameState::InventoryMenu)
			Sound::playSound(GameSound::SoundType::Selector);
		int i = selectorInventoryIndex;
		selectedItem = items[i]->sprite;
		selectedItem.setPosition(itemSelectedPt.x, itemSelectedPt.y);
		selector.setPosition(items[i]->sprite.getPosition().x, items[i]->sprite.getPosition().y);
	}
}
bool Inventory::isPossessingBow(Item* item,int index){
	bool found = false;
	Arrow* arrow = (Arrow*)item;
	if (arrow->bowIsActive){
		selectorInventoryIndex = index;
		found = true;
	}
	return found;
}
void Inventory::update(sf::Event& event){
	getInput(event);
	if (sf::Keyboard::isKeyPressed(sf::Keyboard::Q) && keyWasReleased)
		transitionBackToGame();
}
void Inventory::transitionBackToGame(){
	Static::gameState = GameState::Playing;
	playerBar->movePlayerBarToTopScreen();
	playerBar->itemSlotS=selectedItem;
	keyWasReleased = false;
}
void Inventory::drawInventoryItems(sf::RenderWindow& mainWindow){
	mainWindow.draw(selectedItem);
	for (int i = 0; i < items.size(); i++){
		if (items[i] != NULL && items[i]->isActive)
			mainWindow.draw(items[i]->sprite);
	}
}
void Inventory::drawInventoryText(sf::RenderWindow& mainWindow){
	txt.setCharacterSize(14);
	txt.setColor(sf::Color(228, 68, 55, 255));
	txt.setPosition(inventoryText.x, inventoryText.y);
	txt.setString("INVENTORY");
	mainWindow.draw(txt);

	txt.setColor(sf::Color::White);
	txt.setPosition(itemUseButtonText.x, itemUseButtonText.y);
	txt.setString("USE S BUTTON\n FOR THIS");
	mainWindow.draw(txt);
	float triX = triforce.x;
	float triY = triforce.y;
	int triforceSize = 34;
	sf::ConvexShape testTri;
	testTri.setPointCount(3);
	testTri.setFillColor(sf::Color(255, 165, 66, 255));

	//triforce Dungeon 1
	if (hasDungeon1Triforce){
		testTri.setPoint(0, sf::Vector2f(triX, triY));
		testTri.setPoint(1, sf::Vector2f(triX + triforceSize, triY - triforceSize));
		testTri.setPoint(2, sf::Vector2f(triX + triforceSize, triY));
		mainWindow.draw(testTri);
	}
	//triforce Dungeon 2
	if (hasDungeon2Triforce){
		testTri.setPoint(0, sf::Vector2f(triX + triforceSize, triY));
		testTri.setPoint(1, sf::Vector2f(triX + 2 * triforceSize, triY));
		testTri.setPoint(2, sf::Vector2f(triX + triforceSize, triY - triforceSize));
		mainWindow.draw(testTri);
	}
	//triforce Dungeon 3
	if (hasDungeon3Triforce){
		testTri.setPoint(0, sf::Vector2f(triX - triforceSize, triY + triforceSize));
		testTri.setPoint(1, sf::Vector2f(triX, triY));
		testTri.setPoint(2, sf::Vector2f(triX, triY + triforceSize));
		mainWindow.draw(testTri);
	}
	//triforce Dungeon 4
	if (hasDungeon4Triforce){
		testTri.setPoint(0, sf::Vector2f(triX, triY + triforceSize));
		testTri.setPoint(1, sf::Vector2f(triX + triforceSize, triY + triforceSize));
		testTri.setPoint(2, sf::Vector2f(triX, triY));
		mainWindow.draw(testTri);
	}
	//triforce Dungeon 5
	if (hasDungeon5Triforce){
		testTri.setPoint(0, sf::Vector2f(triX, triY));
		testTri.setPoint(1, sf::Vector2f(triX + triforceSize, triY));
		testTri.setPoint(2, sf::Vector2f(triX + triforceSize, triY + triforceSize));
		mainWindow.draw(testTri);
	}
	//triforce Dungeon 6
	if (hasDungeon6Triforce){
		testTri.setPoint(0, sf::Vector2f(triX + triforceSize, triY + triforceSize));
		testTri.setPoint(1, sf::Vector2f(triX + 2 * triforceSize, triY + triforceSize));
		testTri.setPoint(2, sf::Vector2f(triX + triforceSize, triY));
		mainWindow.draw(testTri);
	}
	//triforce Dungeon 7
	if (hasDungeon7Triforce){
		testTri.setPoint(0, sf::Vector2f(triX + triforceSize, triY));
		testTri.setPoint(1, sf::Vector2f(triX + 2 * triforceSize, triY));
		testTri.setPoint(2, sf::Vector2f(triX + 2 * triforceSize, triY + triforceSize));
		mainWindow.draw(testTri);
	}
	//triforce Dungeon 8
	if (hasDungeon8Triforce){
		testTri.setPoint(0, sf::Vector2f(triX + 2 * triforceSize, triY + triforceSize));
		testTri.setPoint(1, sf::Vector2f(triX + 3 * triforceSize, triY + triforceSize));
		testTri.setPoint(2, sf::Vector2f(triX + 2 * triforceSize, triY));
		mainWindow.draw(testTri);
	}

	sf::ConvexShape triforce;
	triforce.setPointCount(3);
	triforce.setFillColor(sf::Color::Transparent);
	triforce.setOutlineThickness(1);
	triforce.setOutlineColor(sf::Color(255,184,170,255));
	triforce.setPoint(0, sf::Vector2f(triX - triforceSize, triY + triforceSize));
	triforce.setPoint(1, sf::Vector2f(triX + triforceSize, triY-triforceSize));
	triforce.setPoint(2, sf::Vector2f(triX + 3 * triforceSize, triY+triforceSize));
	mainWindow.draw(triforce);

	//bigger triforce frame
	int triFrameSizeDif = 16;
	triforce.setPoint(0, sf::Vector2f(triX - triforceSize - 2*triFrameSizeDif, triY + triforceSize + triFrameSizeDif));
	triforce.setPoint(1, sf::Vector2f(triX + triforceSize, triY - triforceSize - triFrameSizeDif));
	triforce.setPoint(2, sf::Vector2f(triX + 3 * triforceSize + 2*triFrameSizeDif, triY + triforceSize + triFrameSizeDif));
	mainWindow.draw(triforce);

	txt.setColor(sf::Color(228,68,55,255));
	txt.setPosition(triX - triforceSize, triY + triforceSize + 2*triFrameSizeDif);
	txt.setString("TRIFORCE");
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