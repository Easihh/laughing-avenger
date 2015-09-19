#ifndef GAME_H
#define GAME_H
#include "SFML\Graphics.hpp"
class Game{

public:
	Game();
	~Game();
	void Start();
	void GameLoop();
	sf::RenderWindow mainWindow;
private:

};



#endif