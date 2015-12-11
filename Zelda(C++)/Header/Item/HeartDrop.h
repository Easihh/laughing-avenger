#ifndef HEARTDROP_H
#define HEARTDROP_H
#include "Misc\GameObject.h"
#include "Misc\Animation.h"
class HeartDrop :public GameObject {
public:
	HeartDrop(Point pos);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
	void setImage();
	std::unique_ptr<Animation> anim;
};
#endif