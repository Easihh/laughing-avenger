#include "Misc\WorldMap.h"
#include "Misc\Tile.h"
#include "Monster\Octorok.h"
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
	/*pre populate vector of vectors with empty vectors*/
	for (int i = 0; i < Global::WorldRoomWidth; i++){
		mainVectorColums.push_back(roomGameObjects);
		mainBackgroundColumns.push_back(roomGameObjects);
	}
	for (int i = 0; i < Global::WorldRoomHeight; i++){
		gameMainVector.push_back(mainVectorColums);
		gameBackgroundVector.push_back(mainVectorColums);
	}
	vectorXindex = 0;
	vectorYindex = 0;
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
			if (lastWorldXIndex % 16 == 0){
				vectorYindex++;
				if (vectorYindex == Global::WorldRoomWidth)
					vectorYindex = 0;
			}
		}
		lastWorldYIndex++;
		if (lastWorldYIndex % 16 == 0){
			vectorXindex++;
		}
		lastWorldXIndex = 0;
	}
	in.close();
	
}
void WorldMap::createTile(int lastWorldXIndex, int lastWorldYIndex, int tileType){
	GameObject* tile;
	float x = lastWorldXIndex*Global::TileWidth;
	float y = lastWorldYIndex*Global::TileHeight;
	Point pt(x, y + Global::inventoryHeight);
	switch (tileType){
	case -1:
		//no tile;
		break;
	case 0:
		player = new Player(pt);
		break;
	case 1:
		tile = new Tile(pt, false, 1);
		gameBackgroundVector[vectorXindex][vectorYindex].push_back(tile);
		break;
	case 2:
		tile = new Tile(pt, true, 2);
		gameMainVector[vectorXindex][vectorYindex].push_back(tile);
		break;
	case 3:
		tile = new Octorok(pt, false);
		gameMainVector[vectorXindex][vectorYindex].push_back(tile);
		break;
	}
}
//Static::Direction getStartingDirection(){

//}
void WorldMap::update(sf::RenderWindow& mainWindow,sf::Event& event){
	mainWindow.setKeyRepeatEnabled(true);
	if (event.type == sf::Event::KeyReleased && event.key.code == sf::Keyboard::Q)
		player->inventoryKeyReleased = true;
	if (event.type == sf::Event::KeyReleased && event.key.code == sf::Keyboard::Space)
		player->attackKeyReleased = true;
	if (event.type == sf::Event::KeyReleased && event.key.code == sf::Keyboard::S)
		player->itemKeyReleased = true;
		drawAndUpdateCurrentScreen(mainWindow);
		drawRightScreen(mainWindow);
		drawLeftScreen(mainWindow);
		drawUpScreen(mainWindow);
		drawDownScreen(mainWindow);
		player->update(&gameMainVector[player->worldX][player->worldY]);
		player->draw(mainWindow);
		mainWindow.display();
}
void WorldMap::drawScreen(sf::RenderWindow& mainWindow,std::vector<GameObject*> Maplayer){
	for each (GameObject* obj in Maplayer)
	{
			obj->draw(mainWindow);
	}
}
void WorldMap::freeSpace(){
	for each (GameObject* del in Static::toDelete)
	{
		for (int i = 0; i < gameMainVector[player->worldX][player->worldY].size(); i++){
			if (gameMainVector[player->worldX][player->worldY].at(i) == del){
				delete gameMainVector[player->worldX][player->worldY].at(i);
				gameMainVector[player->worldX][player->worldY].erase(gameMainVector[player->worldX][player->worldY].begin() + i);
			}
		}
	}
	Static::toDelete.clear();
}
void WorldMap::addToGameVector(std::vector<GameObject*>* roomObjVector){
	for each (GameObject* obj in Static::toAdd)
	{
		roomObjVector->push_back(obj);
	}
	Static::toAdd.clear();
}
void WorldMap::drawAndUpdateCurrentScreen(sf::RenderWindow& mainWindow){
	freeSpace();
	drawScreen(mainWindow, gameBackgroundVector[player->worldX][player->worldY]);
	for each (GameObject* obj in gameMainVector[player->worldX][player->worldY])
	{
			obj->update(&gameMainVector[player->worldX][player->worldY]);
			obj->draw(mainWindow);
	}
	addToGameVector(&gameMainVector[player->worldX][player->worldY]);
}
void WorldMap::drawRightScreen(sf::RenderWindow& mainWindow){
	if (player->worldY== Global::WorldRoomWidth-1)return;
	drawScreen(mainWindow, gameBackgroundVector[player->worldX][player->worldY+1]);
	drawScreen(mainWindow, gameMainVector[player->worldX][player->worldY+1]);
}
void WorldMap::drawLeftScreen(sf::RenderWindow& mainWindow){
	if (player->worldY== 0)return;
	drawScreen(mainWindow, gameBackgroundVector[player->worldX][player->worldY-1]);
	drawScreen(mainWindow, gameMainVector[player->worldX][player->worldY -1]);
}
void WorldMap::drawUpScreen(sf::RenderWindow& mainWindow){
	if (player->worldX == 0)return;
	drawScreen(mainWindow, gameBackgroundVector[player->worldX-1][player->worldY]);
	drawScreen(mainWindow, gameMainVector[player->worldX-1][player->worldY]);
}
void WorldMap::drawDownScreen(sf::RenderWindow& mainWindow){
	if (player->worldX == Global::WorldRoomHeight-1)return;
	drawScreen(mainWindow, gameBackgroundVector[player->worldX+1][player->worldY]);
	drawScreen(mainWindow, gameMainVector[player->worldX + 1][player->worldY]);
}