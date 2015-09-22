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
private:

};



#endif