#ifndef MOVINGSWORD_H
#define MOVINGSWORD_H
#include "Misc\GameObject.h"
#include "Misc\Animation.h"
class MovingSword :public GameObject {
public:
	MovingSword(Point pos,Direction attackDir, int power);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void draw(sf::RenderWindow& mainWindow);
private:
	std::unique_ptr<Animation> swordAnimation;
	Direction swordDir;
	void loadAnimation(Direction attackDir);
	const int movingSpeed = 5;
	int swordPower;
	void createDestroyEffect();
	void swordMovement(std::vector<std::shared_ptr<GameObject>>* worldMap);
};
#endif