#include "Player\PlayerBar.h"
#include "Utility\Static.h"
PlayerBar::~PlayerBar(){}
PlayerBar::PlayerBar(){
	marker.setPoint(16, 32);
	map.setPoint(16, 32);
	bar.setPoint(0, 0);
	healthBarStart.setPoint(320, 80);
	currentHealthPoint = 16;
	maxHealthPoint = 32;
	itemSlotStart.setPoint(216, 36);
	diamondStart.setPoint(152, 32);
	diamondTextStart.setPoint(155, 30);
	itemSlotTextStart.setPoint(228, 26);
	bombStart.setPoint(150, 96);
	bombTextStart.setPoint(170, 78);
	keyStart.setPoint(150, 56);
	keyTextStart.setPoint(170, 54);
	swordSlot.setPoint(268, 50);
	itemSlotImage.setPoint(220, 50);
	keysAmount = bombAmount = diamondAmount = 10;
	keysPtr = &keysAmount;
	bombPtr = &bombAmount;
	diamondPtr= &diamondAmount;
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
	keyIconTexture.loadFromFile("Tileset/keys_icon.png");
	woodSwordTexture.loadFromFile("Tileset/weapon1.png");
	font.loadFromFile("zelda.ttf");
}
int PlayerBar::getCurrentHP(){
	return currentHealthPoint;
}
void PlayerBar::updatePlayerMapMarker(Static::Direction direction){
	switch (direction){
	case Static::Direction::Down:
		marker.y += Global::playerMarkerHeight;
		break;
	case Static::Direction::Up:
		marker.y -= Global::playerMarkerHeight;
		break;
	case Static::Direction::Right:
		marker.x += Global::playerMarkerWidth;
		break;
	case Static::Direction::Left:
		marker.x -= Global::playerMarkerWidth;
		break;
	}
}
void PlayerBar::decreaseCurrentHP(int amount){
	if (currentHealthPoint >= amount)
		currentHealthPoint -= amount;
}
void PlayerBar::setupMap(){
	overworldMap.setFillColor(sf::Color(128, 128, 128));
	sf::Vector2f size(Global::overworldMapWidth, Global::overworldMapHeight);
	overworldMap.setSize(size);
	overworldMap.setPosition(map.x, map.y);
}
void PlayerBar::setupPlayerBar(){
	playerBar.setFillColor(sf::Color::Black);
	sf::Vector2f size(Global::roomWidth, Global::inventoryHeight);
	playerBar.setSize(size);
	playerBar.setPosition(bar.x, bar.y);
	setupMap();
	setupPlayerMarker();
	txt.setFont(font);
}
void PlayerBar::setupPlayerMarker(){
	playerMarker.setFillColor(sf::Color::Green);
	sf::Vector2f size(Global::playerMarkerWidth, Global::playerMarkerHeight);
	playerMarker.setSize(size);
	playerMarker.setPosition(marker.x, marker.y);
}
void PlayerBar::setBarNextPosition(Point step){
	bar +=step;
	marker+=step;
	map+=(step);
	healthBarStart+=(step);
	itemSlotStart+=(step);
	diamondStart+=(step);
	itemSlotTextStart+=(step);
	bombStart+=(step);
	bombTextStart+=(step);
	diamondTextStart+=(step);
	keyStart+=(step);
	keyTextStart+=(step);
	swordSlot+=(step);
	itemSlotImage+=(step);
}
void PlayerBar::movePlayerBarToBottomScreen(){
	healthBarStart.y += Global::SCREEN_HEIGHT-Global::inventoryHeight;
	itemSlotStart.y += Global::SCREEN_HEIGHT - Global::inventoryHeight;
	itemSlotTextStart.y += Global::SCREEN_HEIGHT - Global::inventoryHeight;
	diamondStart.y += Global::SCREEN_HEIGHT - Global::inventoryHeight;
	diamondTextStart.y += Global::SCREEN_HEIGHT - Global::inventoryHeight;
	bombStart.y += Global::SCREEN_HEIGHT - Global::inventoryHeight;
	bombTextStart.y += Global::SCREEN_HEIGHT - Global::inventoryHeight;
	keyStart.y += Global::SCREEN_HEIGHT - Global::inventoryHeight;
	keyTextStart.y += Global::SCREEN_HEIGHT - Global::inventoryHeight;
	bar.y += Global::SCREEN_HEIGHT - Global::inventoryHeight;
	map.y += Global::SCREEN_HEIGHT - Global::inventoryHeight;
	marker.y += Global::SCREEN_HEIGHT - Global::inventoryHeight;
	swordSlot.y += Global::SCREEN_HEIGHT - Global::inventoryHeight;
	itemSlotImage.y += Global::SCREEN_HEIGHT - Global::inventoryHeight;
}
void PlayerBar::movePlayerBarToTopScreen(){
	healthBarStart.y =healthBarStart.y-Global::SCREEN_HEIGHT + Global::inventoryHeight;
	itemSlotStart.y = itemSlotStart.y - Global::SCREEN_HEIGHT + Global::inventoryHeight;
	itemSlotTextStart.y = itemSlotTextStart.y - Global::SCREEN_HEIGHT + Global::inventoryHeight;
	diamondStart.y = diamondStart.y - Global::SCREEN_HEIGHT + Global::inventoryHeight;
	diamondTextStart.y = diamondTextStart.y - Global::SCREEN_HEIGHT + Global::inventoryHeight;
	bombStart.y = bombStart.y - Global::SCREEN_HEIGHT + Global::inventoryHeight;
	bombTextStart.y = bombTextStart.y - Global::SCREEN_HEIGHT + Global::inventoryHeight;
	keyStart.y = keyStart.y - Global::SCREEN_HEIGHT + Global::inventoryHeight;
	keyTextStart.y = keyTextStart.y - Global::SCREEN_HEIGHT + Global::inventoryHeight;
	bar.y = bar.y - Global::SCREEN_HEIGHT + Global::inventoryHeight;
	marker.y = marker.y - Global::SCREEN_HEIGHT + Global::inventoryHeight;
	swordSlot.y = swordSlot.y - Global::SCREEN_HEIGHT + Global::inventoryHeight;
	map.y = map.y - Global::SCREEN_HEIGHT + Global::inventoryHeight;
	itemSlotImage.y = itemSlotImage.y - Global::SCREEN_HEIGHT + Global::inventoryHeight;
}
void PlayerBar::drawHearts(sf::RenderWindow& mainWindow){
	int totalHearts = maxHealthPoint / 2;
	int heartsDrawn = 0;
	int fullHearthAmount = currentHealthPoint / 2;
	int halfHeartAmount = (maxHealthPoint - currentHealthPoint) % 2;
	int emptyHeartAmount = totalHearts - fullHearthAmount - halfHeartAmount;
	float tmpHealthBarStartX = healthBarStart.x;
	float tmpHealthBarStartY = healthBarStart.y;
	//Draw Full Hearts
	for (int i = 0; i < fullHearthAmount; i++){
		sprite.setTexture(fullHeartTexture,true);
		sprite.setPosition(tmpHealthBarStartX, tmpHealthBarStartY);
		mainWindow.draw(sprite);
		tmpHealthBarStartX += heartWidth;
		heartsDrawn++;
		if (heartsDrawn == maxHeartPerRow){
			tmpHealthBarStartY -= heartHeight;
			tmpHealthBarStartX = healthBarStart.x;
			heartsDrawn = 0;
		}
	}
	//Draw Half-Heart
	for (int i = 0; i < halfHeartAmount; i++){
		sprite.setTexture(halfHeartTexture,true);
		sprite.setPosition(tmpHealthBarStartX, tmpHealthBarStartY);
		mainWindow.draw(sprite);
		tmpHealthBarStartX += heartWidth;
		heartsDrawn++;
		if (heartsDrawn == maxHeartPerRow){
			tmpHealthBarStartY -= heartHeight;
			tmpHealthBarStartX = healthBarStart.x;
			heartsDrawn = 0;
		}
	}
	//Draw Empty-Heart
	for (int i = 0; i < emptyHeartAmount; i++){
		sprite.setTexture(emptyHeartTexture,true);
		sprite.setPosition(tmpHealthBarStartX, tmpHealthBarStartY);
		mainWindow.draw(sprite);
		tmpHealthBarStartX += heartWidth;
		heartsDrawn++;
		if (heartsDrawn == maxHeartPerRow){
			tmpHealthBarStartY -= heartHeight;
			tmpHealthBarStartX = healthBarStart.x;
			heartsDrawn = 0;
		}
	}
}
void PlayerBar::drawItemsSlot(sf::RenderWindow& mainWindow){
	int spaceBetweenBothItemSlot = 8;
	sprite.setTexture(itemSlotTexture,true);
	sprite.setPosition(itemSlotStart.x, itemSlotStart.y);
	mainWindow.draw(sprite);
	sprite.setPosition(itemSlotStart.x + spaceBetweenBothItemSlot + itemSlotTexture.getSize().x, itemSlotStart.y);
	mainWindow.draw(sprite);

	txt.setColor(sf::Color::White);
	txt.setCharacterSize(14);
	txt.setPosition(itemSlotTextStart.x, itemSlotTextStart.y);
	txt.setString("S");
	mainWindow.draw(txt);
	txt.setPosition(itemSlotTextStart.x+spaceBetweenBothItemSlot+itemSlotTexture.getSize().x, itemSlotTextStart.y);
	txt.setString("A");
	mainWindow.draw(txt);

	sprite.setTexture(woodSwordTexture,true);
	sprite.setPosition(swordSlot.x, swordSlot.y);
	mainWindow.draw(sprite);
	if (itemSlotS.getTexture() != NULL){
		itemSlotS.setPosition(itemSlotImage.x, itemSlotImage.y);
		mainWindow.draw(itemSlotS);
	}
}
void PlayerBar::drawBombInfo(sf::RenderWindow& mainWindow){
	ss.str("");
	ss << "X" << bombAmount;
	txt.setCharacterSize(14);
	txt.setColor(sf::Color::White);
	txt.setPosition(bombTextStart.x, bombTextStart.y);
	txt.setString(ss.str());
	mainWindow.draw(txt);
	sprite.setTexture(bombIconTexture,true);
	sprite.setPosition(bombStart.x, bombStart.y - bombIconTexture.getSize().y);
	mainWindow.draw(sprite);
}
void PlayerBar::drawKeyInfo(sf::RenderWindow& mainWindow){
	ss.str("");
	ss << "X" << keysAmount;
	txt.setCharacterSize(14);
	txt.setColor(sf::Color::White);
	txt.setPosition(keyTextStart.x, keyTextStart.y);
	txt.setString(ss.str());
	mainWindow.draw(txt);
	sprite.setTexture(keyIconTexture,true);
	sprite.setPosition(keyStart.x, keyStart.y);
	mainWindow.draw(sprite);
}
void PlayerBar::drawDiamondInfo(sf::RenderWindow& mainWindow){
	ss.str("");
	ss << diamondAmount;
	sprite.setTexture(diamondIconTexture, true);
	sprite.setPosition(diamondStart.x, diamondStart.y);
	mainWindow.draw(sprite);

	txt.setColor(sf::Color::White);
	txt.setString(ss.str());
	txt.setCharacterSize(14);
	txt.setPosition(diamondTextStart.x + diamondIconTexture.getSize().x, diamondTextStart.y);
	mainWindow.draw(txt);
}
void PlayerBar::drawPlayerBar(sf::RenderWindow& mainWindow){
	txt.setString("- L I F E -");
	txt.setColor(sf::Color::Red);
	txt.setPosition(healthBarStart.x, overworldMap.getPosition().y);
	txt.setCharacterSize(12);
	mainWindow.draw(txt);
	drawHearts(mainWindow);
	drawBombInfo(mainWindow);
	drawDiamondInfo(mainWindow);
	drawKeyInfo(mainWindow);
	drawItemsSlot(mainWindow);
}
void PlayerBar::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(playerBar);
	mainWindow.draw(overworldMap);
	mainWindow.draw(playerMarker);
	drawPlayerBar(mainWindow);
}
void PlayerBar::update(){
	playerBar.setPosition(bar.x, bar.y);
	overworldMap.setPosition(map.x, map.y);
	playerMarker.setPosition(marker.x, marker.y);
}