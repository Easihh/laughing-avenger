#ifndef ANIMATION_H
#define ANIMATION_H

#include "SFML\Graphics.hpp"
#include <string>
#include "Static.h"
class Animation{
public:
	Animation();
	Animation(std::string filename, int recHeight, int recWidth, float rectX, float rectY,const int framerate);
	~Animation();
	sf::Sprite sprite;
	void updateAnimationFrame(Static::Direction dir);
	void reset();
private:
	sf::Texture texture;
	sf::IntRect subRect;
	int height, width, maxRow, maxCol,xPosition,yPosition, currentIndex,fRate,fCounter;
};

#endif