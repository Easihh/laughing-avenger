#ifndef GAME_OBJECT_H
#define GAME_OBJECT_H
#include "SFML\Graphics.hpp"
#include "Static.h"
class GameObject{
public:
	GameObject();
	~GameObject();
	virtual void update(GameObject* worldMap[Static::WorldRows][Static::WorldColumns]);
	virtual void draw(sf::RenderWindow& mainWindow);
	float xPosition;
	float yPosition;
	unsigned int width;
	unsigned int height;
	bool isCollideable;
	sf::Texture texture;
	sf::Sprite sprite;
	static bool intersect(GameObject* RectA, GameObject* RectB, int offsetX, int offsetY);
private:
};

#endif