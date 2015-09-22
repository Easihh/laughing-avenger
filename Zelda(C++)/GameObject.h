#ifndef GAME_OBJECT_H
#define GAME_OBJECT_H
#include "SFML\Graphics.hpp"
class GameObject{
public:
	GameObject();
	~GameObject();
	virtual void update();
	virtual void draw(sf::RenderWindow& mainWindow);
	float xPosition;
	float yPosition;
	unsigned int width;
	unsigned int height;
	sf::RectangleShape sprite;
private:
};

#endif