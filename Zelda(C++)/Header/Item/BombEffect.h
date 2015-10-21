#ifndef BOMBEFFECT_H
#define BOMBEFFECT_H
#include "Misc\GameObject.h"
#include "Utility\EffectType.h"
class BombEffect:public GameObject{
public:
	~BombEffect();
	BombEffect(float x,float y,EffectType type);
	virtual void update(std::vector<GameObject*>* worldMap);
	virtual void draw(sf::RenderWindow& mainWindow);
private:
	int currentFrame;
	const int maxFrame = 20;
	EffectType eType;
};
#endif