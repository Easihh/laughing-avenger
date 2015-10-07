#include "WorldMap.h"
#include "Tile.h"
#include "Octorok.h"
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
			//std::cout <<"Row:"<<lastWorldXIndex <<" Col:"<<lastWorldYIndex << " Value:" << *it << std::endl;
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
	GameObject* tile;
	float x = lastWorldXIndex*Global::TileWidth;
	float y = lastWorldYIndex*Global::TileHeight;
	switch (tileType){
	case -1:
		//no tile;
		break;
	case 0:
		player = new Player(x,y + Global::inventoryHeight);
		break;
	case 1:
		tile = new Tile(x, y + Global::inventoryHeight, false, 1);
		worldLayer1[lastWorldXIndex][lastWorldYIndex] = tile;
		break;
	case 2:
		tile = new Tile(x, y + Global::inventoryHeight, true, 2);
		worldLayer2[lastWorldXIndex][lastWorldYIndex] = tile;
		break;
	case 3:
		tile = new Octorok(x, y + Global::inventoryHeight, false);
		worldLayer2[lastWorldXIndex][lastWorldYIndex] = tile;
		break;
	}
}
void WorldMap::update(sf::RenderWindow& mainWindow,sf::Event& event){
	if (event.type == sf::Event::KeyReleased && event.key.code == sf::Keyboard::Q)
		player->inventoryKeyReleased = true;
		drawBackgroundTile(mainWindow);
		drawAndUpdateCurrentScreen(mainWindow);
		drawAndUpdateRightScreen(mainWindow);
		drawAndUpdateLeftScreen(mainWindow);
		drawAndUpdateUpScreen(mainWindow);
		drawAndUpdateDownScreen(mainWindow);
		player->update(worldLayer2);
		player->draw(mainWindow);
		mainWindow.display();
}
void WorldMap::drawBackgroundTile(sf::RenderWindow& mainWindow){
	for (int i = 0; i < Static::WorldRows; i++){
		for (int j = 0; j < Static::WorldColumns; j++){
			if (worldLayer1[i][j] != NULL){
				//worldLayer1[i][j]->update(worldLayer1);//background tile should not change
				worldLayer1[i][j]->draw(mainWindow);
			}
		}
	}
}
void WorldMap::freeSpace(){
	for each (GameObject* obj in toDelete)
	{
		int row = obj->spawnRow;
		int col = obj->spawnCol;
		delete obj;
		worldLayer2[row][col] = NULL;
	}
	toDelete.clear();
}
void WorldMap::drawAndUpdateCurrentScreen(sf::RenderWindow& mainWindow){
	freeSpace();
	float startX = player->worldX*Global::roomRows;
	float startY = player->worldY*Global::roomCols;
	for (int i = startY; i < startY + Global::roomCols; i++){
		for (int j = startX; j < startX + Global::roomRows; j++){
			if (worldLayer2[i][j] != NULL){
				worldLayer2[i][j]->update(worldLayer2);
				worldLayer2[i][j]->draw(mainWindow);
				if (worldLayer2[i][j]->toBeDeleted)
					toDelete.push_back(worldLayer2[i][j]);
			}
		}
	}
}
void WorldMap::drawAndUpdateRightScreen(sf::RenderWindow& mainWindow){
	float startX = (player->worldX)*Global::roomRows;
	float startY = (player->worldY+1)*Global::roomCols;
	if ((player->worldY + 1) * Global::roomCols < Static::WorldColumns){
		for (int i = startY; i < startY + Global::roomCols; i++){
			for (int j = startX; j < startX + Global::roomRows; j++){
				if (worldLayer2[i][j] != NULL)
					worldLayer2[i][j]->draw(mainWindow);
			}
		}
	}
}
void WorldMap::drawAndUpdateLeftScreen(sf::RenderWindow& mainWindow){
	float startX = (player->worldX)*Global::roomRows;
	float startY = (player->worldY - 1)*Global::roomCols;
	if (player->worldY	!=0){
		for (int i = startY; i < startY + Global::roomCols; i++){
			for (int j = startX; j < startX + Global::roomRows; j++){
				if (worldLayer2[i][j] != NULL)
					worldLayer2[i][j]->draw(mainWindow);
			}
		}
	}
}
void WorldMap::drawAndUpdateUpScreen(sf::RenderWindow& mainWindow){
	float startX = (player->worldX-1)*Global::roomRows;
	float startY = (player->worldY)*Global::roomCols;
	if (player->worldX != 0){
		for (int i = startY; i < startY + Global::roomCols; i++){
			for (int j = startX; j < startX + Global::roomRows; j++){
				if (worldLayer2[i][j] != NULL)
					worldLayer2[i][j]->draw(mainWindow);
			}
		}
	}
}
void WorldMap::drawAndUpdateDownScreen(sf::RenderWindow& mainWindow){
	float startX = (player->worldX+1)*Global::roomRows;
	float startY = (player->worldY)*Global::roomCols;
	if ((player->worldX + 1) * Global::roomCols < Static::WorldColumns){
		for (int i = startY; i < startY + Global::roomCols; i++){
			for (int j = startX; j < startX + Global::roomRows; j++){
				if (worldLayer2[i][j] != NULL)
					worldLayer2[i][j]->draw(mainWindow);
			}
		}
	}
}