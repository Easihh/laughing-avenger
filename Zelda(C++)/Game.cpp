#include "Game.h"
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
	case Static::Inventory:
		break;
	}
}
void Game::Start(){
	if (Static::gameState != Static::NotStarted)
		return;
	mainWindow.create(sf::VideoMode(Global::SCREEN_WIDTH, Global::SCREEN_HEIGHT, 32), "Zelda: Final Quest");
	Global::gameView.setSize(Global::SCREEN_WIDTH, Global::SCREEN_HEIGHT);
	Global::gameView.setCenter(Global::SCREEN_WIDTH / 2,Global::SCREEN_HEIGHT / 2);
	mainWindow.setView(Global::gameView);
	mainWindow.setFramerateLimit(Global::FPS_RATE);
	Static::gameState = Static::Playing;
	while (Static::gameState != Static::Exiting){
		GameLoop();
	}
	mainWindow.close();
}