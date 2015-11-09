#ifndef SWORDDESTROYEFFECT_H
#define SWORDDESTROYEFFECT_H
#include "Misc\GameObject.h"
#include "Misc\Animation.h"
class SwordDestroyEffect :public GameObject {
public:
	~SwordDestroyEffect();
	SwordDestroyEffect(Point pos, Static::Direction dir);
	void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
	void draw(sf::RenderWindow& mainWindow);
private:
	void setAnimation();
	std::unique_ptr<Animation> swordEffectAnimation;
	int currentFrame;
	int const maxFrame = 14, movingSpeed = 2;
	void movement();
	Static::Direction direction;
};
#endif