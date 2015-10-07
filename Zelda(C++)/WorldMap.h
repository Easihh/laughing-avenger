#ifndef WORLD_MAP_H
#define WORLD_MAP_H

#include <sstream>
#include <string>
#include <fstream>
#include <boost/algorithm/string.hpp>
#include <vector>
#include <iostream>
#include "Player.h"
class WorldMap{
public:
	WorldMap();
	~WorldMap();
	void loadMap(std::string filename);
	GameObject* worldLayer1[Static::WorldRows][Static::WorldColumns];
	GameObject* worldLayer2[Static::WorldRows][Static::WorldColumns];
	void update(sf::RenderWindow& mainWindow,sf::Event& event);
	Player* player;
private:
	int lastWorldXIndex,lastWorldYIndex;
	void createTile(int lastWorldXIndex, int lastWorldYIndex, int tileType);
	void drawBackgroundTile(sf::RenderWindow& mainWindow);
	void drawAndUpdateCurrentScreen(sf::RenderWindow& mainWindow);
	void drawAndUpdateRightScreen(sf::RenderWindow& mainWindow);
	void drawAndUpdateLeftScreen(sf::RenderWindow& mainWindow);
	void drawAndUpdateDownScreen(sf::RenderWindow& mainWindow);
	void drawAndUpdateUpScreen(sf::RenderWindow& mainWindow);
	std::vector<GameObject*> toDelete;
	void freeSpace();
};
#endif