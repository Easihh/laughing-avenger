#include "PlayerBar.h"
#include "Static.h"
PlayerBar::~PlayerBar(){}
PlayerBar::PlayerBar(){
	markerX = 48;
	markerY = 32;
	mapX = 48;
	mapY = 32;
	barX = 0;
	barY = 0;
	currentHealthPoint = 15;
	maxHealthPoint = 32;
	setupPlayerBar();
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
	fullHeartTexture.loadFromFile("Tileset/Life_Full.png");
	emptyHeartTexture.loadFromFile("Tileset/Life_Empty.png");
	halfHeartTexture.loadFromFile("Tileset/Life_Half.png");
	itemSlotTexture.loadFromFile("Tileset/Itemslot.png");
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
}
void PlayerBar::drawHearts(sf::RenderWindow& mainWindow){
	int totalHearts = maxHealthPoint / 2;
	int heartsDrawn = 0;
	int fullHearthAmount = currentHealthPoint / 2;
	int halfHeartAmount = (maxHealthPoint - currentHealthPoint) % 2;
	int emptyHeartAmount = totalHearts - fullHearthAmount - halfHeartAmount;
	float healthBarStartX = overworldMap.getPosition().x+304;
	float healthBarStartY = overworldMap.getPosition().y+48;

	//Draw Full Hearts
	for (int i = 0; i < fullHearthAmount; i++){
		sprite.setTexture(fullHeartTexture,true);
		sprite.setPosition(healthBarStartX, healthBarStartY);
		mainWindow.draw(sprite);
		healthBarStartX += heartWidth;
		heartsDrawn++;
		if (heartsDrawn == maxHeartPerRow){
			healthBarStartY -= heartHeight;
			healthBarStartX -= heartsDrawn*heartWidth;
			heartsDrawn = 0;
		}
	}
	//Draw Half-Heart
	for (int i = 0; i < halfHeartAmount; i++){
		sprite.setTexture(halfHeartTexture);
		sprite.setPosition(healthBarStartX, healthBarStartY);
		mainWindow.draw(sprite);
		healthBarStartX += heartWidth;
		heartsDrawn++;
		if (heartsDrawn == maxHeartPerRow){
			healthBarStartY -= heartHeight;
			healthBarStartX -= heartsDrawn*heartWidth;
			heartsDrawn = 0;
		}
	}
	//Draw Empty-Heart
	for (int i = 0; i < emptyHeartAmount; i++){
		sprite.setTexture(emptyHeartTexture);
		sprite.setPosition(healthBarStartX, healthBarStartY);
		mainWindow.draw(sprite);
		healthBarStartX += heartWidth;
		heartsDrawn++;
		if (heartsDrawn == maxHeartPerRow){
			healthBarStartY -= heartHeight;
			healthBarStartX -= heartsDrawn*heartWidth;
			heartsDrawn = 0;
		}
	}
}
void PlayerBar::drawPlayerBarImg(sf::RenderWindow& mainWindow){
	sprite.setTexture(itemSlotTexture,true);
	sprite.setPosition(overworldMap.getPosition().x + Global::overworldMapWidth+64, overworldMap.getPosition().y+4);
	mainWindow.draw(sprite);
	sprite.setPosition(sprite.getPosition().x + 48, sprite.getPosition().y);
	mainWindow.draw(sprite);
}
void PlayerBar::drawPlayerBarText(sf::RenderWindow& mainWindow){
	sf::Font font;
	font.loadFromFile("zelda.ttf");
	sf::Text txt("- L I F E -", font);
	txt.setColor(sf::Color::Red);
	txt.setPosition(overworldMap.getPosition().x + 304, overworldMap.getPosition().y);
	txt.setCharacterSize(12);
	mainWindow.draw(txt);
	txt.setColor(sf::Color::White);
	txt.setPosition(overworldMap.getPosition().x + 208, overworldMap.getPosition().y-4);
	txt.setString("S");
	mainWindow.draw(txt);
	txt.setPosition(overworldMap.getPosition().x + 256, overworldMap.getPosition().y - 4);
	txt.setString("A");
	mainWindow.draw(txt);
}
void PlayerBar::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(playerBar);
	mainWindow.draw(overworldMap);
	mainWindow.draw(playerMarker);
	drawHearts(mainWindow);
	drawPlayerBarImg(mainWindow);
	drawPlayerBarText(mainWindow);
}
void PlayerBar::update(){
	barX = playerBar.getPosition().x;
	barY = playerBar.getPosition().y;
	markerX = playerMarker.getPosition().x;
	markerY = playerMarker.getPosition().y;
	mapX = overworldMap.getPosition().x;
	mapY = overworldMap.getPosition().y;
}