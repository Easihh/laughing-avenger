#ifndef PLAYER_BAR_H
#define PLAYER_BAR_H

#include "SFML\Graphics.hpp"
class PlayerBar{
public:
	PlayerBar();
	~PlayerBar();
	void setupPlayerBar();
	void setupMap();
	void setupPlayerMarker();
	void draw(sf::RenderWindow& mainWindow);
	void update();
	void setBarNextPosition(float stepX, float stepY);
	sf::RectangleShape playerMarker, playerBar, overworldMap;
private:
	float barX, barY, mapX, mapY, markerX, markerY;
};
#endif