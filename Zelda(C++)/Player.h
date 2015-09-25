#ifndef PLAYER_H
#define PLAYER_H
#include "SFML\Graphics.hpp"
#include "GameObject.h"
class Player:public GameObject{
public:
	Player(float x,float y);
	~Player();
	void update(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]);
	void draw(sf::RenderWindow& mainWindow);
	enum Direction{Right,Left,Up,Down};
private:
	const int minStep = 16;
	unsigned int stepToMove;
	Direction dir;
	void completeMove();
	bool isColliding(GameObject* worldLayer[Static::WorldRows][Static::WorldColumns]);
	int xOffset, yOffset;
	int getXOffset();
	int getYOffset();
	void loadImage();
	sf::IntRect subRect;
	int walkAnimationIndex;
	void updateAnimationFrame();
};
#endif