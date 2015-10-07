#include "Inventory.h"
#include "Static.h"
Inventory::Inventory(){
	keyWasReleased = false;
	inventoryRect.setOutlineColor(sf::Color(64,0,128));
	inventoryRect.setOutlineThickness(3);
	inventoryRect.setFillColor(sf::Color::Transparent);
	sf::Vector2f size(Global::SCREEN_WIDTH/2, 96);
	inventoryRect.setSize(size);
}
Inventory::~Inventory(){}
void Inventory::transitionToInventory(PlayerBar* playerBar){
	playerBar->playerBar.setPosition(playerBar->playerBar.getPosition().x, playerBar->playerBar.getPosition().y + Global::SCREEN_HEIGHT - Global::inventoryHeight);
	playerBar->overworldMap.setPosition(playerBar->overworldMap.getPosition().x, playerBar->overworldMap.getPosition().y + Global::SCREEN_HEIGHT-Global::inventoryHeight);
	playerBar->playerMarker.setPosition(playerBar->playerMarker.getPosition().x, playerBar->playerMarker.getPosition().y + Global::SCREEN_HEIGHT - Global::inventoryHeight);
	inventoryRect.setPosition(playerBar->overworldMap.getPosition().x + 144, playerBar->overworldMap.getPosition().y -416);
}
void Inventory::getInput(sf::Event& event){
	if (event.type == sf::Event::KeyReleased && event.key.code == sf::Keyboard::Q)
		keyWasReleased = true;
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
}
void Inventory::draw(sf::RenderWindow& mainWindow,PlayerBar* playerBar){
	mainWindow.draw(inventoryRect);
	playerBar->draw(mainWindow);
	mainWindow.display();
}