#ifndef GAME_OBJECT_H
#define GAME_OBJECT_H
#include "SFML\Graphics.hpp"
#include "Utility\Point.h"
#include <memory>
#include "Misc\Sound.h"
#include "Misc\SoundType.h"
class GameObject{
public:
	GameObject();
	~GameObject();
	virtual void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	virtual void draw(sf::RenderWindow& mainWindow);
	void destroyGameObject(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	Point position;
	unsigned int width,height;
	bool isCollideable;
	int depth;
	void setupFullMask();
	sf::Texture texture;
	sf::Sprite sprite;
	std::unique_ptr<sf::RectangleShape> fullMask;
	bool isOutsideRoomBound(Point pos);
	std::shared_ptr<GameObject> collidingMonster;
	bool isCollidingWithMonster(std::vector<std::shared_ptr<GameObject>>* worldMap);
	static bool intersect(std::unique_ptr<sf::RectangleShape>& rectA, std::unique_ptr<sf::RectangleShape>& rectB, Point offset);
private:
};

#endif