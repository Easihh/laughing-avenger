#ifndef OCTOROK_H
#define OCTOROK_H

#include "Monster.h"
#include <string>
class Octorok :public Monster{
public:
	Octorok(Point position, bool canBeCollidedWith);
	~Octorok();
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<GameObject*>* worldMap);
private:
	void loadImage(std::string filename);
};
#endif