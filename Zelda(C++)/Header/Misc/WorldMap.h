#ifndef WORLD_MAP_H
#define WORLD_MAP_H

#include <sstream>
#include <string>
#include <fstream>
#include <boost/algorithm/string.hpp>
#include <vector>
#include <iostream>
#include "Player\Player.h"
class WorldMap{
public:
	typedef std::vector<std::vector<std::vector<std::shared_ptr<GameObject>>>> tripleVector;
	WorldMap();
	~WorldMap();
	void loadMap(std::string filename,tripleVector& triple);
	tripleVector gameMainVector, gameBackgroundVector, secretRoomVector, secretRoomBackgroundVector;
	std::vector<std::vector<std::shared_ptr<GameObject>>> mainVectorColums, mainBackgroundColumns,secretRoomColumns,secretRoomBackgroundColumns;
	std::vector<std::shared_ptr<GameObject>> roomGameObjects, roomBackGroundTile,secretRoomTile,secretRoomGameObjects;
	void update(sf::RenderWindow& mainWindow,sf::Event& event);
	std::shared_ptr<Player> player;
	void movePlayerToDifferentRoomVector(int oldWorldX, int oldWorldY, int newWorldX, int newWorldY);
private:
	int lastWorldXIndex, lastWorldYIndex, vectorXindex, vectorYindex;
	void createTile(int lastWorldXIndex, int lastWorldYIndex, int tileType, tripleVector& objectVector);
	void drawScreen(sf::RenderWindow& mainWindow, std::vector<std::shared_ptr<GameObject>>* Maplayer);
	void drawAndUpdateCurrentScreen(sf::RenderWindow& mainWindow);
	void drawRightScreen(sf::RenderWindow& mainWindow);
	void drawLeftScreen(sf::RenderWindow& mainWindow);
	void drawDownScreen(sf::RenderWindow& mainWindow);
	void drawUpScreen(sf::RenderWindow& mainWindow);
	void freeSpace(tripleVector&);
	void addToGameVector(std::vector<std::shared_ptr<GameObject>>* roomVector);
	void setupVectors();
	void sort(tripleVector& objectVector);
};
#endif