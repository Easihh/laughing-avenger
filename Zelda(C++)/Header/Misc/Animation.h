#ifndef ANIMATION_H
#define ANIMATION_H

#include "SFML\Graphics.hpp"
#include <string>
#include "Utility\Direction.h"
#include "Utility\Point.h"
class Animation{
public:
	Animation();
	Animation(std::string filename, int recHeight, int recWidth, Point position,const int framerate);
	~Animation();
	sf::Sprite sprite;
	void updateAnimationFrame(Direction dir, Point position);
	void setSubRectangle(Direction dir);
	void updateAnimationFrame(Point position);
	void reset();
	int currentIndex;
private:
	sf::Texture texture;
	sf::IntRect subRect;
	int height, width, maxRow, maxCol,xPosition,yPosition,fRate,fCounter;
};

#endif