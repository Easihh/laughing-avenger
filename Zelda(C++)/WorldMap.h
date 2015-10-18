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
	std::vector<std::vector<std::vector<GameObject*>>> gameMainVector,gameBackgroundVector;
	std::vector<std::vector<GameObject*>> mainVectorColums,mainBackgroundColumns;
	std::vector<GameObject*> roomGameObjects,roomBackGroundTile;
	void update(sf::RenderWindow& mainWindow,sf::Event& event);
	Player* player;
private:
	int lastWorldXIndex, lastWorldYIndex, vectorXindex, vectorYindex;
	void createTile(int lastWorldXIndex, int lastWorldYIndex, int tileType);
	void drawScreen(sf::RenderWindow& mainWindow,std::vector<GameObject*> mapLayer);
	void drawAndUpdateCurrentScreen(sf::RenderWindow& mainWindow);
	void drawRightScreen(sf::RenderWindow& mainWindow);
	void drawLeftScreen(sf::RenderWindow& mainWindow);
	void drawDownScreen(sf::RenderWindow& mainWindow);
	void drawUpScreen(sf::RenderWindow& mainWindow);
	std::vector<GameObject*> toDelete;
	void freeSpace();
};
#endif