#include "Game.h"
#include "GameManager.h"
#include "Static.h"
#include "Player.h"
#include "Block.h"
#include <sstream>
GameManager gameManager;
Game::Game(){}
Game::~Game(){}
void Game::GameLoop(){
	sf::Event event;
	mainWindow.pollEvent(event);
	if (event.type == sf::Event::Closed)
		Static::gameState = Static::Exiting;
	switch (Static::gameState){
	case Static::Playing:
		gameManager.updateAll(mainWindow);
		break;
	}
}
void Game::Start(){
	if (Static::gameState != Static::NotStarted)
		return;
	mainWindow.create(sf::VideoMode(Static::SCREEN_WIDTH, Static::SCREEN_HEIGHT, 32), "Zelda: Final Quest");
	mainWindow.setFramerateLimit(60);
	Player* player = new Player();
	Block* block = new Block();
	gameManager.add("Player",player);
	gameManager.add("Block", block);
	Static::gameState = Static::Playing;
	while (Static::gameState != Static::Exiting){
		GameLoop();
	}
	mainWindow.close();
}