#ifndef GAME_OBJECT_H
#define GAME_OBJECT_H
#include "SFML\Graphics.hpp"
#include "Utility\Point.h"
#include <memory>
#include "Misc\Sound.h"
#include "Type\SoundType.h"
#include "Type\Direction.h"
class GameObject{
public:
	GameObject();
	virtual void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	virtual void draw(sf::RenderWindow& mainWindow);
	void destroyGameObject(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	Point position;
	unsigned int width,height;
	bool isCollideable;
	int depth, pushbackStep;
	const float pushBackMaxDistance = 96;
	const int stepPerPushBackUpdate = 4;
	void setupFullMask();
	void pushbackUpdate();
	sf::Texture texture;
	sf::Sprite sprite;
	std::unique_ptr<sf::RectangleShape> fullMask;
	bool isOutsideRoomBound(Point pos);
	std::shared_ptr<GameObject> findPlayer(std::vector<std::shared_ptr<GameObject>>* worldMap);
	std::shared_ptr<GameObject> findBoomerang(std::vector<std::shared_ptr<GameObject>>* worldMap);
	std::shared_ptr<GameObject> collidingMonster;
	bool isCollidingWithMonster(std::vector<std::shared_ptr<GameObject>>* worldMap);
	Direction dir;
	void pushBack(std::vector<std::shared_ptr<GameObject>>* worldMap,Direction attackDir);
	int GameObject::getDistanceToMapBoundary(Direction direction);
	bool GameObject::isCollidingWithPlayer(std::vector<std::shared_ptr<GameObject>>* worldMap);
	bool GameObject::isCollidingWithBoomerang(std::vector<std::shared_ptr<GameObject>>* worldMap);
	bool GameObject::isColliding(std::vector<std::shared_ptr<GameObject>>* worldMap, std::unique_ptr<sf::RectangleShape>& mask, Point offsets);
	int getMinimumLineCollisionDistance(Direction pushbackDir, std::vector<std::shared_ptr<GameObject>>* worldMap);
	static bool intersect(std::unique_ptr<sf::RectangleShape>& rectA, std::unique_ptr<sf::RectangleShape>& rectB, Point offset);
	static bool intersect(std::unique_ptr<sf::RectangleShape>& rectA, std::unique_ptr<sf::RectangleShape>& rectB);
	static void deleteNpcFromCurrentRoom(std::vector<std::shared_ptr<GameObject>>* worldMap);
private:
};

#endif