#ifndef BOMBEFFECT_H
#define BOMBEFFECT_H
#include "Misc\GameObject.h"
#include "Utility\EffectType.h"
class BombEffect:public GameObject{
public:
	~BombEffect();
	BombEffect(Point position,EffectType type);
	virtual void update(std::vector<std::shared_ptr<GameObject>>* worldMap);
	virtual void draw(sf::RenderWindow& mainWindow);
private:
	int currentFrame;
	const int maxFrame = 20,bombDmg=1;
	EffectType eType;
	void collisionWithMonster(std::vector<std::shared_ptr<GameObject>>* worldMap);
};
#endif