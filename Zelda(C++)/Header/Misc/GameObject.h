#ifndef GAME_OBJECT_H
#define GAME_OBJECT_H
#include "SFML\Graphics.hpp"
#include "Utility\Point.h"
class GameObject{
public:
	GameObject();
	~GameObject();
	virtual void update(std::vector<GameObject*>* worldMap);
	virtual void draw(sf::RenderWindow& mainWindow);
	Point position;
	unsigned int width,height;
	bool isCollideable;
	bool toBeDeleted;
	void setupFullMask();
	sf::Texture texture;
	sf::Sprite sprite;
	sf::RectangleShape* fullMask;
	static bool intersect(sf::RectangleShape* rectA, sf::RectangleShape* rectB, Point offset);
private:
};

#endif