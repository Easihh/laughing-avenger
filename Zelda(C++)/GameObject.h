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
	float xPosition,yPosition;
	unsigned int width,height,spawnCol,spawnRow;
	bool isCollideable;
	bool toBeDeleted;
	void setupFullMask();
	sf::Texture texture;
	sf::Sprite sprite;
	sf::RectangleShape* fullMask;
	static bool intersect(sf::RectangleShape* rectA, sf::RectangleShape* rectB, int offsetX, int offsetY);
private:
};

#endif