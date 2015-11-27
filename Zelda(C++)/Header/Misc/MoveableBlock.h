#ifndef MOVEABLEBLOCK_H
#define MOVEABLEBLOCK_H
#include "Misc\GameObject.h"
class MoveableBlock:public GameObject{
public:
	MoveableBlock(Point pos);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void draw(sf::RenderWindow& mainWindow);
	Point originalPosition;
	void resetPosition();
private:
	void checkIfPlayerIsNear(std::vector<std::shared_ptr<GameObject>>* worldMap);
	bool hasBeenPushed;
};

#endif