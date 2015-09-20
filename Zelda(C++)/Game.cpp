#include "Game.h"
#include "GameManager.h"
#include "Static.h"
#include "Player.h"
GameManager gameManager;
Game::Game(){}
Game::~Game(){}
void Game::GameLoop(){
	sf::Event event;
	mainWindow.pollEvent(event);
	if (event.type == sf::Event::Closed)
		Static::gameState = Static::Exiting;
	mainWindow.setFramerateLimit(Static::FPS_RATE);
	switch (Static::gameState){
	case Static::Playing:
		mainWindow.clear(sf::Color::Black);
		gameManager.updateAll(mainWindow);
		mainWindow.display();
		break;
	}
}
void Game::Start(){
	if (Static::gameState != Static::NotStarted)
		return;
	mainWindow.create(sf::VideoMode(Static::SCREEN_WIDTH, Static::SCREEN_HEIGHT, 32), "Zelda: Final Quest");
	Player* player = new Player();
	gameManager.add("Player",player);
	Static::gameState = Static::Playing;
	while (Static::gameState != Static::Exiting){
		GameLoop();
	}
	mainWindow.close();
}