#ifndef PLAYER_H
#define PLAYER_H
#include "SFML\Graphics.hpp"
#include "GameObject.h"
class Player:public GameObject{
public:
	Player(float x,float y);
	~Player();
	void update();
	void draw(sf::RenderWindow& mainWindow);
	enum Direction{Right,Left,Up,Down};
private:
	const int minStep = 16;
	unsigned int stepToMove;
	Direction dir;
	void completeMove();
	bool isColliding();
	bool collision;
	int xOffset, yOffset;
	int getXOffset();
	int getYOffset();
	void loadImage();
};
#endif