#ifndef FLAME_H
#define FLAME_H
#include "Misc\GameObject.h"
#include "Misc\Animation.h"
#include "Misc\Tile.h"
class Flame :public Tile {
public:
	Flame(Point pos, bool canBeCollidedWith);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
	std::unique_ptr<Animation> flameAnimation;
};
#endif