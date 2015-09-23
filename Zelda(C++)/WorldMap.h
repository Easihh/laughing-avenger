#ifndef WORLD_MAP_H
#define WORLD_MAP_H

#include <sstream>
#include <string>
#include <fstream>
#include <boost/algorithm/string.hpp>
#include <vector>
#include <iostream>
#include "GameObject.h"
#include "Player.h"
class WorldMap{
public:
	WorldMap();
	~WorldMap();
	void loadMap(std::string filename);
	static const int WorldRows = 16;
	static const int WorldColumns = 16;
	const int TileWidth = 32;
	const int TileHeight = 32;
	GameObject* worldLayer1[WorldMap::WorldRows][WorldMap::WorldColumns];
	GameObject* worldLayer2[WorldMap::WorldRows][WorldMap::WorldColumns];
	int FPS_REFRESH_RATE = 1000;
	unsigned int fpsCounter = 0;
	sf::Time timePerFrame = sf::seconds(1.0f / 60.0f);
	sf::Time timeSinceLastUpdate = sf::Time::Zero;
	sf::Time fpsTimer = sf::Time::Zero;
	sf::Clock timerClock, fpsClock;
	void update(sf::RenderWindow& mainWindow);
private:
	std::ifstream in;
	std::string line;
	std::vector<std::string> strs;
	int lastWorldXIndex,lastWorldYIndex;
	void createTile(int lastWorldXIndex, int lastWorldYIndex, int tileType);
	Player* player;
};
#endif