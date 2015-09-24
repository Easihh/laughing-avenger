#ifndef WORLD_MAP_H
#define WORLD_MAP_H

#include <sstream>
#include <string>
#include <fstream>
#include <boost/algorithm/string.hpp>
#include <vector>
#include <iostream>
#include "Player.h"
#include "Tile.h"
class WorldMap{
public:
	WorldMap();
	~WorldMap();
	void loadMap(std::string filename);
	GameObject* worldLayer1[Static::WorldRows][Static::WorldColumns];
	GameObject* worldLayer2[Static::WorldRows][Static::WorldColumns];
	int FPS_REFRESH_RATE = 1000;
	unsigned int fpsCounter = 0;
	sf::Time timePerFrame = sf::seconds(1.0f / 60.0f);
	sf::Time timeSinceLastUpdate = sf::Time::Zero;
	sf::Time fpsTimer = sf::Time::Zero;
	sf::Clock timerClock, fpsClock;
	void update(sf::RenderWindow& mainWindow);
private:
	Tile* backgroundTile;
	std::ifstream in;
	std::string line;
	std::vector<std::string> strs;
	int lastWorldXIndex,lastWorldYIndex;
	void createTile(int lastWorldXIndex, int lastWorldYIndex, int tileType);
	Player* player;
};
#endif