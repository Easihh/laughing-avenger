#ifndef TRIFORCE_H
#define TRIFORCE_H
#include "Misc\GameObject.h"
class Triforce :public GameObject {
public:
	Triforce(Point pos);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
	int currentFrame;
	const int maxFrame = 420;
	bool isObtained;
};

#endif