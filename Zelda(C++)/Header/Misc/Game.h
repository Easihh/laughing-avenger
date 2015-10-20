#ifndef GAME_H
#define GAME_H
#include "SFML\Graphics.hpp"
#include "WorldMap.h"
class Game{

public:
	Game();
	~Game();
	void Start();
	void GameLoop();
	sf::RenderWindow mainWindow;
	WorldMap world;
	int FPS_REFRESH_RATE = 1000;
	unsigned int fpsCounter = 0;
	sf::Time timePerFrame = sf::seconds(1.0f / 60.0f);
	sf::Time timeSinceLastUpdate = sf::Time::Zero;
	sf::Time fpsTimer = sf::Time::Zero;
	sf::Clock timerClock, fpsClock;
private:

};



#endif