#ifndef PLAYER_H
#define PLAYER_H
#include "SFML\Graphics.hpp"
#include "GameObject.h"

class Player:public GameObject{
public:
	sf::RectangleShape sprite;
	Player();
	~Player();
	void update(std::map<std::string, GameObject*> mapObjects);
	void draw(sf::RenderWindow& mainWindow);
	enum Direction{Right,Left,Up,Down};
private:
	const int minStep = 16;
	unsigned int stepToMove;
	Direction dir;
	void completeMove();
	bool isColliding(std::map<std::string, GameObject*> mapObjects);
	bool collision;
	int xOffset, yOffset;
	int getXOffset();
	int getYOffset();
};
#endif