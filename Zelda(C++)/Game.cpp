#include "Game.h"
#include "Static.h"
#include "Player.h"
#include "WorldMap.h"
Game::Game(){}
Game::~Game(){}
void Game::GameLoop(){
	sf::Event event;
	mainWindow.pollEvent(event);
	if (event.type == sf::Event::Closed)
		Static::gameState = Static::Exiting;
	switch (Static::gameState){
	case Static::Playing:
		world.update(mainWindow);
		break;
	}
}
void Game::Start(){
	if (Static::gameState != Static::NotStarted)
		return;
	mainWindow.create(sf::VideoMode(Static::SCREEN_WIDTH, Static::SCREEN_HEIGHT, 32), "Zelda: Final Quest");
	mainWindow.setFramerateLimit(60);
	Static::gameState = Static::Playing;
	while (Static::gameState != Static::Exiting){
		GameLoop();
	}
	mainWindow.close();
}