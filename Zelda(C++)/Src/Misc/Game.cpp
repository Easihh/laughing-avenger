#include "Misc\Game.h"
Game::Game(){}
Game::~Game(){}
void Game::GameLoop(){
	sf::Event event;
	mainWindow.pollEvent(event);
	if (event.type == sf::Event::Closed)
		Static::gameState = Static::Exiting;
	switch (Static::gameState){
	case Static::Playing:
		world.update(mainWindow,event);
		break;
	case Static::Inventory:
		world.player->inventory->update(event,world.player->playerBar);
		world.player->inventory->draw(mainWindow,world.player->playerBar);
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
		timeSinceLastUpdate += timerClock.restart();
		fpsTimer += fpsClock.restart();
		if (fpsTimer.asMilliseconds() >= FPS_REFRESH_RATE){
			std::stringstream title;
			title << Static::GAME_TITLE << "FPS:" << fpsCounter;
			mainWindow.setTitle(title.str());
			fpsCounter = 0;
			fpsTimer = sf::Time::Zero;
		}
		if (timeSinceLastUpdate.asMilliseconds() >= 1667){
			mainWindow.clear(sf::Color::Black);
			fpsCounter++;
			timeSinceLastUpdate -= timePerFrame;
			GameLoop();
		}
	}
	mainWindow.close();
}