#include "Player\PlayerBar.h"
#include "Utility\Static.h"
PlayerBar::PlayerBar(Point start){
	currentDungeon = DungeonLevel::NONE;
	maxBombAmount = 8;
	mySword = SwordType::None;
	int worldX = start.y / Global::roomHeight;
	int worldY = start.x / Global::roomWidth;
	marker.setPoint(start.x + 16 +(worldY*Global::playerMarkerHeight), start.y + 32+(worldX*Global::playerMarkerWidth));
	map.setPoint(start.x + 16, start.y+32);
	bar.setPoint(start.x, start.y);
	healthBarStart.setPoint(start.x + 320, start.y + 80);
	currentHealthPoint = 32;
	maxHealthPoint = 32;
	itemSlotStart.setPoint(start.x + 216, start.y + 36);
	diamondStart.setPoint(start.x + 152, start.y + 32);
	diamondTextStart.setPoint(start.x + 155, start.y + 30);
	itemSlotTextStart.setPoint(start.x + 228, start.y + 26);
	bombStart.setPoint(start.x + 150, start.y + 96);
	bombTextStart.setPoint(start.x + 170, start.y + 78);
	keyStart.setPoint(start.x + 150, start.y + 56);
	keyTextStart.setPoint(start.x + 170, start.y + 54);
	swordSlot.setPoint(start.x + 268, start.y+ 50);
	itemSlotImage.setPoint(start.x + 220, start.y + 50);
	keysAmount = bombAmount = 1;
	diamondAmount = 255;
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
void PlayerBar::increaseRupeeAmount(int amount) {
	diamondAmount += amount;
	if(diamondAmount > maxRupee)
		diamondAmount = maxRupee;
}
void PlayerBar::increaseBombAmount(int amount) {
	bombAmount += amount;
	if(bombAmount> maxBombAmount){
		bombAmount = maxBombAmount;
	}
}
bool PlayerBar::isFullHP() {
	return currentHealthPoint == maxHealthPoint;
}
void PlayerBar::increaseMaxHP() {
	//used for Heart Container
	int hpIncrease = 2;//1 for half-heart and 2 for a whole heart
	maxHealthPoint += hpIncrease;
	currentHealthPoint += hpIncrease;
}
void PlayerBar::updatePlayerMapMarker(Direction direction){
	switch (direction){
	case Direction::Down:
		if (currentDungeon==DungeonLevel::NONE)
			marker.y += Global::playerMarkerHeight;
		else dungeonMarker.y += Global::playerMarkerHeight;
		break;
	case Direction::Up:
		if (currentDungeon == DungeonLevel::NONE)
			marker.y -= Global::playerMarkerHeight;
		else dungeonMarker.y -= Global::playerMarkerHeight;
		break;
	case Direction::Right:
		if (currentDungeon == DungeonLevel::NONE)
			marker.x += Global::playerMarkerWidth;
		else dungeonMarker.x += Global::HalfTileWidth;
		break;
	case Direction::Left:
		if (currentDungeon == DungeonLevel::NONE)
			marker.x -= Global::playerMarkerWidth;
		else dungeonMarker.x -= Global::HalfTileWidth;
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

	dungeonMap.setFillColor(sf::Color::Black);
	//sf::Vector2f size(Global::overworldMapWidth, Global::overworldMapHeight);
	dungeonMap.setSize(size);
	dungeonMap.setPosition(map.x, map.y);
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

	dungeonPlayerMarker.setFillColor(sf::Color::Green);
	dungeonPlayerMarker.setSize(size);
	dungeonPlayerMarker.setPosition(dungeonMarker.x, dungeonMarker.y);
}
void PlayerBar::setBarNextPosition(Point step){
	bar +=step;
	marker+=step;
	dungeonMarker += step;
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
void PlayerBar::setPlayerBar(Point pt){
	int worldX = pt.y / Global::roomHeight;
	int worldY = pt.x / Global::roomWidth;
	bar.setPoint(pt.x,pt.y);
	marker.setPoint(pt.x + 16 + (worldY*Global::playerMarkerHeight), pt.y+32 + (worldX*Global::playerMarkerWidth));
	map.setPoint(pt.x+16, pt.y+32);
	healthBarStart.setPoint(pt.x+320, pt.y+80);
	itemSlotStart.setPoint(pt.x+216, pt.y+36);
	diamondStart.setPoint(pt.x+152, pt.y+32);
	itemSlotTextStart.setPoint(pt.x+228, pt.y+26);
	bombStart.setPoint(pt.x+150, pt.y+96);
	bombTextStart.setPoint(pt.x+170, pt.y+78);
	diamondTextStart.setPoint(pt.x+155, pt.y+30);
	keyStart.setPoint(pt.x+150, pt.y+56);
	keyTextStart.setPoint(pt.x+170, pt.y+54);
	swordSlot.setPoint(pt.x+268, pt.y+50);
	itemSlotImage.setPoint(pt.x+220, pt.y+50);
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
	dungeonMarker.y += Global::SCREEN_HEIGHT - Global::inventoryHeight;
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
	dungeonMarker.y = dungeonMarker.y - Global::SCREEN_HEIGHT + Global::inventoryHeight;
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

	if(mySword == SwordType::WoodSword){
		sprite.setTexture(woodSwordTexture, true);
		sprite.setPosition(swordSlot.x, swordSlot.y);
	}
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
void PlayerBar::drawDungeonMap(sf::RenderWindow& mainWindow){
	sf::RectangleShape dungeonMapRect;
	int spaceBetweenDungeonRect = 1;
	sf::Vector2f size(15,7);
	dungeonMapRect.setFillColor(sf::Color(110, 93, 243));
	dungeonMapRect.setSize(size);
	std::stringstream pos;
	pos << "LEVEL-" << (int)currentDungeon;
	sf::Text txt(pos.str(), font);
	txt.setPosition(bar.x+16, bar.y+16);
	txt.setColor(sf::Color::White);
	txt.setCharacterSize(14);
	mainWindow.draw(txt);
	mainWindow.draw(dungeonMap);
	dungeonMapRect.setPosition(map.x+56, map.y + 56);
	mainWindow.draw(dungeonMapRect);
	dungeonMapRect.setPosition(map.x+72, map.y + 56);
	mainWindow.draw(dungeonMapRect);
	dungeonMapRect.setPosition(map.x + 72, map.y + 48);
	mainWindow.draw(dungeonMapRect);
	dungeonMapRect.setPosition(map.x + 56, map.y + 48);
	mainWindow.draw(dungeonMapRect);
	dungeonMapRect.setPosition(map.x + 40, map.y + 48);
	mainWindow.draw(dungeonMapRect);
	dungeonMapRect.setPosition(map.x + 40, map.y + 56);
	mainWindow.draw(dungeonMapRect);
	dungeonMapRect.setPosition(map.x + 24, map.y + 48);
	mainWindow.draw(dungeonMapRect);
	dungeonMapRect.setPosition(map.x + 24, map.y + 56 );
	mainWindow.draw(dungeonMapRect);
	dungeonMapRect.setPosition(map.x + 56, map.y + 40);
	mainWindow.draw(dungeonMapRect);
	dungeonMapRect.setPosition(map.x + 40, map.y + 40);
	mainWindow.draw(dungeonMapRect);
	dungeonMapRect.setPosition(map.x + 56, map.y + 32);
	mainWindow.draw(dungeonMapRect);
	dungeonMapRect.setPosition(map.x + 72, map.y + 32);
	mainWindow.draw(dungeonMapRect);
}
void PlayerBar::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(playerBar);
	if (currentDungeon == DungeonLevel::NONE){
		mainWindow.draw(overworldMap);
		mainWindow.draw(playerMarker);
	}
	else {
		drawDungeonMap(mainWindow);
		mainWindow.draw(dungeonPlayerMarker);
	}
	drawPlayerBar(mainWindow);
}
void PlayerBar::resetDungeonPlayerMarker(){
	dungeonMarker.x = map.x+60;
	dungeonMarker.y = map.y+56;
}
void PlayerBar::update(){
	playerBar.setPosition(bar.x, bar.y);
	overworldMap.setPosition(map.x, map.y);
	dungeonMap.setPosition(map.x, map.y);
	playerMarker.setPosition(marker.x, marker.y);
	dungeonPlayerMarker.setPosition(dungeonMarker.x, dungeonMarker.y);
}