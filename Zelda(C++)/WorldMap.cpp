#include "WorldMap.h"
#include "Tile.h"
WorldMap::WorldMap(){
	loadMap("Map/Zelda-Worldmap_Layer 1.csv");
	loadMap("Map/Zelda-Worldmap_Layer 2.csv");
}
WorldMap::~WorldMap(){

}
void WorldMap::loadMap(std::string filename){
	std::ifstream in;
	std::string line;
	std::vector<std::string> strs;
	lastWorldXIndex = 0;
	lastWorldYIndex = 0;
	in.open(filename);
	if (in.fail())
		std::cout << "Failed To open:" + filename<<std::endl;
	while (!in.eof()){
		std::getline(in, line, '\n');
		boost::split(strs, line, boost::is_any_of(","));
		for (std::vector<std::string>::iterator it = strs.begin(); it < strs.end(); it++){
			std::cout <<"Row:"<<lastWorldXIndex <<" Col:"<<lastWorldYIndex << " Value:" << *it << std::endl;
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
	Tile* tile;
	Player* player;
	float x = lastWorldXIndex*Global::TileWidth;
	float y = lastWorldYIndex*Global::TileHeight;
	switch (tileType){
	case -1:
		//no tile;
		break;
	case 0:
		player = new Player(x,y + Global::inventoryHeight);
		worldLayer2[lastWorldXIndex][lastWorldYIndex] = player;
		break;
	case 1:
		tile = new Tile(x, y + Global::inventoryHeight, false, 1);
		worldLayer1[lastWorldXIndex][lastWorldYIndex] = tile;
		break;
	case 2:
		tile = new Tile(x,y + Global::inventoryHeight, true, 2);
		worldLayer2[lastWorldXIndex][lastWorldYIndex] = tile;
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
		for (int i = 0; i < Static::WorldRows; i++){
			for (int j = 0; j < Static::WorldColumns; j++){
				if (worldLayer1[i][j] != NULL){
					//worldLayer1[i][j]->update(worldLayer1);//background tile should not change
					worldLayer1[i][j]->draw(mainWindow);
				}
			}
		}
		for (int i = 0; i < Static::WorldRows; i++){
			for (int j = 0; j < Static::WorldColumns; j++){
				if (worldLayer2[i][j] != NULL){
					worldLayer2[i][j]->update(worldLayer2);
					worldLayer2[i][j]->draw(mainWindow);
				}
			}
		}
		mainWindow.display();
	}
}