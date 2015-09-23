#include "WorldMap.h"
#include "Static.h"
#include "Tile.h"
WorldMap::WorldMap(){
	loadMap("Map/Zelda-Worldmap_Layer 1.csv");
	loadMap("Map/Zelda-Worldmap_Layer 2.csv");
}
WorldMap::~WorldMap(){

}
void WorldMap::loadMap(std::string filename){
	lastWorldXIndex = 0;
	lastWorldYIndex = 0;
	in.open(filename);
	while (!in.eof()){
		std::getline(in, line, '\n');
		boost::split(strs, line, boost::is_any_of(","));
		for (std::vector<std::string>::iterator it = strs.begin(); it < strs.end(); it++){
			std::cout << "Value:" << *it << std::endl;
			std::string val = *it;
			createTile(lastWorldXIndex, lastWorldYIndex, atoi(val.c_str()));
			lastWorldXIndex++;
		}
		lastWorldYIndex++;
		lastWorldXIndex = 0;
	}
	in.close();
	
}
void WorldMap::createTile(int lastWorldXIndex, int lastWorldYIndex, int tileType){
	switch (tileType){
	case -1:
		//Dummy tile;
		Tile* backgroundTile;
		backgroundTile = new Tile(lastWorldXIndex * 0, lastWorldYIndex * TileHeight);
		worldLayer2[lastWorldYIndex][lastWorldXIndex] = backgroundTile;
		break;
	case 0:
		backgroundTile = new Tile(lastWorldXIndex * TileWidth, lastWorldYIndex * TileHeight);
		worldLayer2[lastWorldYIndex][lastWorldXIndex] = backgroundTile;
		break;
	case 1:
		backgroundTile = new Tile(lastWorldXIndex * TileWidth, lastWorldYIndex * TileHeight);
		worldLayer1[lastWorldYIndex][lastWorldXIndex] = backgroundTile;
		break;
	}
}
void WorldMap::update(sf::RenderWindow& mainWindow){
	timeSinceLastUpdate += timerClock.restart();
	fpsTimer += fpsClock.restart();
	if (fpsTimer.asMilliseconds() >= FPS_REFRESH_RATE){
		std::stringstream title;
		title << Static::GAME_TITLE << "FPS:" << fpsCounter;
		mainWindow.setTitle(title.str());
		fpsCounter = 0;
		fpsTimer = sf::Time::Zero;
	}
	if (timeSinceLastUpdate.asMilliseconds() >= 1667){
		mainWindow.clear(sf::Color::Black);
		fpsCounter++;
		timeSinceLastUpdate -= timePerFrame;
		for (int i = 0; i < WorldRows; i++){
			for (int j = 0; j < WorldColumns; j++){
				worldLayer1[i][j]->update();
				worldLayer1[i][j]->draw(mainWindow);
			}
		}
		for (int i = 0; i < WorldRows; i++){
			for (int j = 0; j < WorldColumns; j++){
				worldLayer2[i][j]->update();
				worldLayer2[i][j]->draw(mainWindow);
			}
		}
		mainWindow.display();
	}
}