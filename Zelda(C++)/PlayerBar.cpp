#include "PlayerBar.h"
#include "Static.h"
PlayerBar::~PlayerBar(){}
PlayerBar::PlayerBar(){
	markerX = mapX = 16;
	markerY = mapY = 32;
	barX = barY = 0;
	healthBarStartX = 320;
	healthBarStartY = 80;
	currentHealthPoint = 15;
	maxHealthPoint = 32;
	itemSlotStartX = 216;
	itemSlotStartY = 36;
	diamondStartX = 152;
	diamondStartY = 32;
	diamondTextStartX = 152;
	diamondTextStartY = 30;
	itemSlotTextStartX = 228;
	itemSlotTextStartY = 26;
	bombStartX = 152;
	bombStartY = 96;
	bombTextStartX = 170;
	bombTextStartY = 78;
	loadImages();
	setupPlayerBar();
}
void PlayerBar::loadImages(){
	fullHeartTexture.loadFromFile("Tileset/Life_Full.png");
	emptyHeartTexture.loadFromFile("Tileset/Life_Empty.png");
	halfHeartTexture.loadFromFile("Tileset/Life_Half.png");
	itemSlotTexture.loadFromFile("Tileset/Itemslot.png");
	diamondIconTexture.loadFromFile("Tileset/rupees_icon.png");
	bombIconTexture.loadFromFile("Tileset/bomb_icon.png");
	font.loadFromFile("zelda.ttf");
}
int PlayerBar::getCurrentHP(){
	return currentHealthPoint;
}
void PlayerBar::decreaseCurrentHP(int amount){
	currentHealthPoint -= amount;
}
void PlayerBar::setupMap(){
	overworldMap.setFillColor(sf::Color(128, 128, 128));
	sf::Vector2f size(Global::overworldMapWidth, Global::overworldMapHeight);
	overworldMap.setSize(size);
	overworldMap.setPosition(mapX, mapY);
}
void PlayerBar::setupPlayerBar(){
	playerBar.setFillColor(sf::Color::Black);
	sf::Vector2f size(Global::roomWidth, Global::inventoryHeight);
	playerBar.setSize(size);
	playerBar.setPosition(barX, barY);
	setupMap();
	setupPlayerMarker();
	txt.setFont(font);
}
void PlayerBar::setupPlayerMarker(){
	playerMarker.setFillColor(sf::Color::Green);
	sf::Vector2f size(Global::playerMarkerWidth, Global::playerMarkerHeight);
	playerMarker.setSize(size);
	playerMarker.setPosition(markerX, markerY);
}
void PlayerBar::setBarNextPosition(float stepX,float stepY){
	playerBar.setPosition(barX + stepX, barY + stepY);
	playerMarker.setPosition(markerX + stepX, markerY + stepY);
	overworldMap.setPosition(mapX + stepX, mapY + stepY);
	healthBarStartX += stepX;
	healthBarStartY += stepY;
	itemSlotStartX += stepX;
	itemSlotStartY += stepY;
	diamondStartX += stepX;
	diamondStartY += stepY;
	itemSlotTextStartX += stepX;
	itemSlotTextStartY += stepY;
	bombStartX += stepX;
	bombStartY += stepY;
	bombTextStartX += stepX;
	bombTextStartY += stepY;
	diamondTextStartX += stepX;
	diamondTextStartY += stepY;
}
void PlayerBar::movePlayerBarToBottomScreen(){
	healthBarStartY += Global::SCREEN_HEIGHT-Global::inventoryHeight;
	itemSlotStartY += Global::SCREEN_HEIGHT - Global::inventoryHeight;
	diamondStartY += Global::SCREEN_HEIGHT - Global::inventoryHeight;
	itemSlotTextStartY += Global::SCREEN_HEIGHT - Global::inventoryHeight;
	bombStartY += Global::SCREEN_HEIGHT - Global::inventoryHeight;
	bombTextStartY += Global::SCREEN_HEIGHT - Global::inventoryHeight;
	diamondTextStartY += Global::SCREEN_HEIGHT - Global::inventoryHeight;
}
void PlayerBar::movePlayerBarToTopScreen(){
	healthBarStartY =healthBarStartY-Global::SCREEN_HEIGHT + Global::inventoryHeight;
	itemSlotStartY = itemSlotStartY - Global::SCREEN_HEIGHT + Global::inventoryHeight;
	diamondStartY = diamondStartY - Global::SCREEN_HEIGHT + Global::inventoryHeight;
	itemSlotTextStartY = itemSlotTextStartY - Global::SCREEN_HEIGHT + Global::inventoryHeight;
	bombStartY = bombStartY - Global::SCREEN_HEIGHT + Global::inventoryHeight;
	bombTextStartY = bombTextStartY - Global::SCREEN_HEIGHT + Global::inventoryHeight;
	diamondTextStartY = diamondTextStartY - Global::SCREEN_HEIGHT + Global::inventoryHeight;
}
void PlayerBar::drawHearts(sf::RenderWindow& mainWindow){
	int totalHearts = maxHealthPoint / 2;
	int heartsDrawn = 0;
	int fullHearthAmount = currentHealthPoint / 2;
	int halfHeartAmount = (maxHealthPoint - currentHealthPoint) % 2;
	int emptyHeartAmount = totalHearts - fullHearthAmount - halfHeartAmount;
	float tmpHealthBarStartX = healthBarStartX;
	float tmpHealthBarStartY = healthBarStartY;
	//Draw Full Hearts
	for (int i = 0; i < fullHearthAmount; i++){
		sprite.setTexture(fullHeartTexture,true);
		sprite.setPosition(tmpHealthBarStartX, tmpHealthBarStartY);
		mainWindow.draw(sprite);
		tmpHealthBarStartX += heartWidth;
		heartsDrawn++;
		if (heartsDrawn == maxHeartPerRow){
			tmpHealthBarStartY -= heartHeight;
			tmpHealthBarStartX = healthBarStartX;
			heartsDrawn = 0;
		}
	}
	//Draw Half-Heart
	for (int i = 0; i < halfHeartAmount; i++){
		sprite.setTexture(halfHeartTexture);
		sprite.setPosition(tmpHealthBarStartX, tmpHealthBarStartY);
		mainWindow.draw(sprite);
		tmpHealthBarStartX += heartWidth;
		heartsDrawn++;
		if (heartsDrawn == maxHeartPerRow){
			tmpHealthBarStartY -= heartHeight;
			tmpHealthBarStartX = healthBarStartX;
			heartsDrawn = 0;
		}
	}
	//Draw Empty-Heart
	for (int i = 0; i < emptyHeartAmount; i++){
		sprite.setTexture(emptyHeartTexture);
		sprite.setPosition(tmpHealthBarStartX, tmpHealthBarStartY);
		mainWindow.draw(sprite);
		tmpHealthBarStartX += heartWidth;
		heartsDrawn++;
		if (heartsDrawn == maxHeartPerRow){
			tmpHealthBarStartY -= heartHeight;
			tmpHealthBarStartX = healthBarStartX;
			heartsDrawn = 0;
		}
	}
}
void PlayerBar::drawItemsSlot(sf::RenderWindow& mainWindow){
	int spaceBetweenBothItemSlot = 8;
	sprite.setTexture(itemSlotTexture,true);
	sprite.setPosition(itemSlotStartX, itemSlotStartY);
	mainWindow.draw(sprite);
	sprite.setPosition(itemSlotStartX + spaceBetweenBothItemSlot + itemSlotTexture.getSize().x, itemSlotStartY);
	mainWindow.draw(sprite);

	txt.setColor(sf::Color::White);
	txt.setCharacterSize(14);
	txt.setPosition(itemSlotTextStartX, itemSlotTextStartY);
	txt.setString("S");
	mainWindow.draw(txt);
	txt.setPosition(itemSlotTextStartX+spaceBetweenBothItemSlot+itemSlotTexture.getSize().x, itemSlotTextStartY);
	txt.setString("A");
	mainWindow.draw(txt);
}
void PlayerBar::drawBombInfo(sf::RenderWindow& mainWindow){
	txt.setCharacterSize(14);
	txt.setColor(sf::Color::White);
	txt.setPosition(bombTextStartX, bombTextStartY);
	txt.setString("X99");
	mainWindow.draw(txt);
	sprite.setTexture(bombIconTexture);
	sprite.setPosition(bombStartX, bombStartY - bombIconTexture.getSize().y);
	mainWindow.draw(sprite);
}
void PlayerBar::drawDiamondInfo(sf::RenderWindow& mainWindow){
	sprite.setTexture(diamondIconTexture, true);
	sprite.setPosition(diamondStartX, diamondStartY);
	mainWindow.draw(sprite);

	txt.setColor(sf::Color::White);
	txt.setString("999");
	txt.setCharacterSize(14);
	txt.setPosition(diamondTextStartX + diamondIconTexture.getSize().x, diamondTextStartY);
	mainWindow.draw(txt);
}
void PlayerBar::drawPlayerBar(sf::RenderWindow& mainWindow){
	txt.setString("- L I F E -");
	txt.setColor(sf::Color::Red);
	txt.setPosition(healthBarStartX, overworldMap.getPosition().y);
	txt.setCharacterSize(12);
	mainWindow.draw(txt);
	drawHearts(mainWindow);
	drawBombInfo(mainWindow);
	drawDiamondInfo(mainWindow);
	drawItemsSlot(mainWindow);
}
void PlayerBar::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(playerBar);
	mainWindow.draw(overworldMap);
	mainWindow.draw(playerMarker);
	drawPlayerBar(mainWindow);
}
void PlayerBar::update(){
	barX = playerBar.getPosition().x;
	barY = playerBar.getPosition().y;
	markerX = playerMarker.getPosition().x;
	markerY = playerMarker.getPosition().y;
	mapX = overworldMap.getPosition().x;
	mapY = overworldMap.getPosition().y;
}