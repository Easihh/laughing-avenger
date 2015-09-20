#ifndef PLAYER_H
#define PLAYER_H
#include "SFML\Graphics.hpp"
#include "GameObject.h"

class Player:public GameObject{
public:
	sf::RectangleShape sprite;
	Player();
	~Player();
	void update();
	void draw(sf::RenderWindow& mainWindow);
	const unsigned int PLAYER_HEIGHT = 32;
	const unsigned int PLAYER_WIDTH = 32;
	unsigned int xPosition;
	unsigned int yPosition;
private:

};
#endif