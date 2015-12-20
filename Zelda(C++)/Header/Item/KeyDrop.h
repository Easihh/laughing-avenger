#ifndef KEYDROP_H
#define KEYDROP_H
#include "Misc\GameObject.h"
#include "Misc\Animation.h"
class KeyDrop :public GameObject {
public:
	KeyDrop(Point pos);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
	void setImage();
};
#endif