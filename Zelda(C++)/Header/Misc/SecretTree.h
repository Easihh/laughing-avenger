#ifndef SECRETTREE_H
#define SECRETTREE_H
#include "Misc\Tile.h"
class SecretTree :public Tile {
public:
	SecretTree(Point position);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
	bool isActivated;
private:
};
#endif