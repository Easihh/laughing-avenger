#ifndef OCTOROK_H
#define OCTOROK_H

#include "Monster.h"
#include "Misc\Animation.h"
#include <string>
class Octorok :public Monster{
public:
	Octorok(Point position, bool canBeCollidedWith);
	~Octorok();
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<GameObject*>* worldMap);
private:
	void loadImage(std::string filename);
	void movement(std::vector<GameObject*>* worldMap);
	bool isColliding(std::vector<GameObject*>* worldMap);
	int getXOffset();
	int getYOffset();
	void getNextDirection(Static::Direction blockedDir);
	bool isOutsideRoomBound(Point pos);
	Animation* anim;
	Static::Direction dir;
	const int minStep = 1;
};
#endif