#ifndef THROWNARROW_H
#define THROWNARROW_H
#include "Misc\GameObject.h"
#include "Utility\Static.h"
#include "Misc\Animation.h"
#include <SFML/Audio.hpp>
class ThrownArrow :public GameObject {
public:
	ThrownArrow(Point pos,Direction direction);
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
private:
	void setup();
	Direction arrowDir;
	void arrowMovement();
	const int arrowSpeed = 6,arrowStrength=1;
};
#endif