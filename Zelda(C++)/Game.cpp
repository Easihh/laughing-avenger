#include "Game.h"
#include "GameManager.h"
Game::GameState Game::gameState = NotStarted;
GameManager gameManager;
Game::Game(){}
Game::~Game(){}
void Game::GameLoop(){
	sf::Event event;
	mainWindow.pollEvent(event);
	if (event.type == sf::Event::Closed)
		gameState = Game::Exiting;
	mainWindow.setFramerateLimit(Game::FPS_RATE);
	switch (gameState){
	case Game::Playing:
		mainWindow.clear(sf::Color(0, 0, 0,255));
		gameManager.updateAll();
		mainWindow.display();
		break;
	}
}
void Game::Start(){
	if (gameState != Game::NotStarted)
		return;
	mainWindow.create(sf::VideoMode(SCREEN_WIDTH, SCREEN_HEIGHT, 32), "Zelda: Final Quest");
	gameState = Game::Playing;
	while (gameState!=Game::Exiting){
		GameLoop();
	}
	mainWindow.close();
}