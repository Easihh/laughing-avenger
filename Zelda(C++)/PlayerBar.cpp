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
	setupPlayerBar();
}
void PlayerBar::setupMap(){
	overworldMap.setFillColor(sf::Color(128, 128, 128));
	sf::Vector2f size(128, 64);
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
void PlayerBar::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(playerBar);
	mainWindow.draw(overworldMap);
	mainWindow.draw(playerMarker);
}
void PlayerBar::update(){
	barX = playerBar.getPosition().x;
	barY = playerBar.getPosition().y;
	markerX = playerMarker.getPosition().x;
	markerY = playerMarker.getPosition().y;
	mapX = overworldMap.getPosition().x;
	mapY = overworldMap.getPosition().y;
}