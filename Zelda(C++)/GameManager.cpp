#include "GameManager.h"
#include <algorithm>
#include "Game.h"
#include <sstream>
#include "Static.h"
#include "Player.h"
GameManager::GameManager(){}
std::vector<GameObject*> test;
GameManager::~GameManager()
{
	std::for_each(gameObjects.begin(), gameObjects.end(), GameObjectDeallocator());
}
void GameManager::updateAll(sf::RenderWindow& mainWindow){
	std::map<std::string, GameObject*>::iterator itr = gameObjects.begin();
	timeSinceLastUpdate += timerClock.restart();
	fpsTimer += fpsClock.restart();
	if (fpsTimer.asMilliseconds() >= FPS_REFRESH_RATE){
		std::stringstream title;
		title <<Static::GAME_TITLE <<"FPS:"<< fpsCounter;
		mainWindow.setTitle(title.str());
		fpsCounter = 0;
		fpsTimer = sf::Time::Zero;
	}
	if (timeSinceLastUpdate.asMilliseconds() >= 1667){
		mainWindow.clear(sf::Color::Black);
		fpsCounter++;
		timeSinceLastUpdate -= timePerFrame;
		itr = gameObjects.begin();
		while (itr != gameObjects.end()){
			itr->second->update(gameObjects);
			itr->second->draw(mainWindow);
			itr++;
		}
		mainWindow.display();
	}
}
void GameManager::add(std::string key,GameObject* obj){
	gameObjects.insert(std::pair<std::string, GameObject*>(key,obj));
	test.push_back(obj);
}
void GameManager::remove(std::string key){
	std::map<std::string, GameObject*>::iterator results = gameObjects.find(key);
	if (results != gameObjects.end()){
		delete results->second;
		gameObjects.erase(results);
	}

}