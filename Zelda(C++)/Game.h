#ifndef GAME_H
#define GAME_H
#include "SFML\Graphics.hpp"
class Game{

public:
	Game();
	~Game();
	const static unsigned int FPS_RATE = 60;
	const static unsigned int SCREEN_WIDTH = 1024;
	const static unsigned int SCREEN_HEIGHT = 680;
	static enum GameState{NotStarted,Paused,Playing,Menu,Exiting};
	static GameState gameState;
	void Start();
	void GameLoop();
	sf::RenderWindow mainWindow;
private:

};



#endif