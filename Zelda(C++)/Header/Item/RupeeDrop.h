#ifndef RUPEEDROP_H
#define RUPEEDROP_H
#include "Misc\GameObject.h"
#include "Type\RupeeType.h"
#include "Misc\Animation.h"
class RupeeDrop :public GameObject {
public:
	RupeeDrop(Point pos,RupeeType type);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
	RupeeType rtype;
	void setImage();
	int rupeeValue;
	std::unique_ptr<Animation> anim;
};
#endif