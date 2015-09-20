#ifndef GAME_MANAGER_H
#define GAME_MANAGER_H
#include <string>
#include "GameObject.h"
#include <map>
#include "SFML\Graphics.hpp"
class GameManager{
public:
	GameManager();
	~GameManager();
	void updateAll(sf::RenderWindow& mainWindow);
	void add(std::string key, GameObject* obj);
	void remove(std::string key);
	int FPS_REFRESH_RATE = 1000;
	unsigned int fpsCounter = 0;
	sf::Time timePerFrame = sf::seconds(1.0f / 60.0f);
	sf::Time timeSinceLastUpdate = sf::Time::Zero;
	sf::Time fpsTimer = sf::Time::Zero;
	sf::Clock timerClock, fpsClock;
private:
	std::map<std::string, GameObject*> gameObjects;
	struct GameObjectDeallocator{
		void operator()(const std::pair<std::string, GameObject*>&p) const
		{
			delete p.second;
		}
	};
};

#endif