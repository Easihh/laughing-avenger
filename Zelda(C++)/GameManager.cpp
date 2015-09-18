#include "GameManager.h"
#include <algorithm>
GameManager::GameManager(){}
GameManager::~GameManager()
{
	std::for_each(gameObjects.begin(), gameObjects.end(), GameObjectDeallocator());
}
void GameManager::updateAll(){

}
void GameManager::add(std::string key,GameObject* obj){
	gameObjects.insert(std::pair<std::string, GameObject*>(key,obj));
}
void GameManager::remove(std::string key){
	std::map<std::string, GameObject*>::iterator results = gameObjects.find(key);
	if (results != gameObjects.end()){
		delete results->second;
		gameObjects.erase(results);
	}

}