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
	void decreaseCurrentHP(int amount);
	int getCurrentHP();
	sf::RectangleShape playerMarker, playerBar, overworldMap;
private:
	float barX, barY, mapX, mapY, markerX, markerY;
	sf::Texture fullHeartTexture,halfHeartTexture,emptyHeartTexture,itemSlotTexture;
	sf::Sprite sprite;
	void drawHearts(sf::RenderWindow& mainWindow);
	void drawPlayerBarText(sf::RenderWindow& mainWindow);
	void drawPlayerBarImg(sf::RenderWindow& mainWindow);
	const int heartWidth = 16,heartHeight=16,maxHeartPerRow=8;
	int currentHealthPoint, maxHealthPoint;
};
#endif