#ifndef ZORAPROJECTILE_H
#define ZORAPROJECTILE_H
#include "Monster\Monster.h"
#include "Utility\Static.h"
#include "Misc\Animation.h"
#include <SFML/Audio.hpp>
class ZoraProjectile :public Monster {
public:
	ZoraProjectile(Point pos, Direction dir);
	void draw(sf::RenderWindow& mainWindow);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
private:
	void setup();
	bool isBeingDestroyed;
	void projectileMovement(std::vector<std::shared_ptr<GameObject>>* worldMap);
	const float projectileSpeed = 2, maxDistance = Global::roomWidth / 4;
	void checkIfPlayerCanBlock(std::vector<std::shared_ptr<GameObject>>* worldMap);
	std::unique_ptr<Animation> projectileAnimation;
};
#endif