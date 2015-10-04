#ifndef OCTOROK_H
#define OCTOROK_H

#include "Monster.h"
#include <string>
class Octorok :public Monster{
public:
	Octorok(float x, float y, bool canBeCollidedWith);
	~Octorok();
	void draw(sf::RenderWindow& mainWindow);
	void update(GameObject* worldMap[Static::WorldRows][Static::WorldColumns]);
private:
	void loadImage(std::string filename);
};
#endif