#include "Misc\Game.h"
#include "Misc\Sound.h"
Game::Game(){}
void Game::GameLoop(){
	sf::Event event;
	mainWindow.pollEvent(event);
	if (event.type == sf::Event::Closed)
		Static::gameState = Exiting;
	switch (Static::gameState){
	case LoadSaveMenu:
		saveLoad.update(event);
		saveLoad.draw(mainWindow);
		break;
	case Playing:
		world.update(mainWindow,event);
		break;
	case InventoryMenu:
		world.player->inventory->update(event);
		world.player->inventory->draw(mainWindow);
		break;
	}
}
void Game::Start(){
	if (Static::gameState != NotStarted)
		return;
	mainWindow.create(sf::VideoMode(Global::SCREEN_WIDTH, Global::SCREEN_HEIGHT, 32), "Zelda: Final Quest");
	Global::gameView.setSize(Global::SCREEN_WIDTH, Global::SCREEN_HEIGHT);
	Global::gameView.setCenter(Global::SCREEN_WIDTH / 2,Global::SCREEN_HEIGHT / 2);
	mainWindow.setView(Global::gameView);
	mainWindow.setFramerateLimit(Global::FPS_RATE);
	Static::gameState = LoadSaveMenu;
	Sound gameSound;
	while (Static::gameState != Exiting){
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