#include "Misc\WorldMap.h"
#include "Misc\Tile.h"
#include "Monster\Octorok.h"
#include "Utility\TileType.h"
#include "Utility\Identifier.h"
#include "Misc\ShopMarker.h"
WorldMap::WorldMap(){
	setupVectors();
	loadMap("Map/Zelda-Worldmap_Layer 1.csv", gameBackgroundVector);
	loadMap("Map/Zelda-Worldmap_Layer 2.csv", gameMainVector);
	loadMap("Map/Zelda-Shop_Layer 1.csv", secretRoomBackgroundVector);
	loadMap("Map/Zelda-Shop_Layer 2.csv", secretRoomVector);
	sort(gameMainVector);
	sort(secretRoomVector);
}
WorldMap::~WorldMap(){}
bool sortByDepth(std::shared_ptr<GameObject> object1, std::shared_ptr<GameObject> object2) {
	return object1.get()->depth<object2.get()->depth;
}
void WorldMap::sort(tripleVector& objectVector) {
	for(int i = 0; i < Global::WorldRoomWidth; i++){
		for(int j = 0; j < Global::WorldRoomHeight; j++){
			std::sort(objectVector[i][j].begin(), objectVector[i][j].end(),sortByDepth);
		}
	}
}
void WorldMap::setupVectors() {
	/*pre populate vector of vectors with empty vectors*/
	for(int i = 0; i < Global::WorldRoomWidth; i++){
		mainBackgroundColumns.push_back(roomBackGroundTile);
		secretRoomBackgroundColumns.push_back(secretRoomTile);
		mainVectorColums.push_back(roomGameObjects);
		secretRoomColumns.push_back(secretRoomGameObjects);
	}
	for(int i = 0; i < Global::WorldRoomHeight; i++){
		gameMainVector.push_back(mainVectorColums);
		secretRoomVector.push_back(secretRoomColumns);
		gameBackgroundVector.push_back(mainBackgroundColumns);
		secretRoomBackgroundVector.push_back(secretRoomBackgroundColumns);
	}
}
void WorldMap::loadMap(std::string filename,tripleVector& objectVector){
	std::ifstream in;
	std::string line;
	std::vector<std::string> strs;
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
			createTile(lastWorldXIndex, lastWorldYIndex, atoi(val.c_str()), objectVector);
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
void WorldMap::createTile(int lastWorldXIndex, int lastWorldYIndex, int tileType, tripleVector& objectVector) {
	std::shared_ptr<GameObject> tile;
	float x = lastWorldXIndex*Global::TileWidth;
	float y = lastWorldYIndex*Global::TileHeight;
	Point pt(x, y + Global::inventoryHeight);
	switch (tileType){
	case -1:
		//no tile;
		break;
	case Identifier::Player_ID:
		player = std::make_shared<Player>(pt);
		objectVector[vectorXindex][vectorYindex].push_back(player);
		break;
	case Identifier::Sand_ID:
		tile =std::make_shared<Tile>(pt, false, TileType::Sand);
		objectVector[vectorXindex][vectorYindex].push_back(tile);
		break;
	case Identifier::GreenTree_ID:
		tile = std::make_shared<Tile>(pt, true, TileType::GreenTree);
		objectVector[vectorXindex][vectorYindex].push_back(tile);
		break;
	case Identifier::RedOctorok_ID:
		tile = std::make_shared<Octorok>(pt, false);
		objectVector[vectorXindex][vectorYindex].push_back(tile);
		break;
	case Identifier::BlackTile_ID:
		tile = std::make_shared<Tile>(pt, false,TileType::BlackTile);
		objectVector[vectorXindex][vectorYindex].push_back(tile);
		break;
	case Identifier::ShopMarker_ID:
		tile = std::make_shared<ShopMarker>(pt);
		objectVector[vectorXindex][vectorYindex].push_back(tile);
		break;
	case Identifier::BrownBlock1_ID:
		tile = std::make_shared<Tile>(pt, true, TileType::BrownBlockType1);
		objectVector[vectorXindex][vectorYindex].push_back(tile);
		break;
	}
}
void WorldMap::update(sf::RenderWindow& mainWindow,sf::Event& event){
	mainWindow.setKeyRepeatEnabled(true);
	if (event.type == sf::Event::KeyReleased && event.key.code == sf::Keyboard::Q)
		player->inventoryKeyReleased = true;
	if (event.type == sf::Event::KeyReleased && event.key.code == sf::Keyboard::Space)
		player->attackKeyReleased = true;
	if (event.type == sf::Event::KeyReleased && event.key.code == sf::Keyboard::S)
		player->itemKeyReleased = true;
		drawRightScreen(mainWindow);
		drawLeftScreen(mainWindow);
		drawUpScreen(mainWindow);
		drawDownScreen(mainWindow);
		drawAndUpdateCurrentScreen(mainWindow);
		/*if(!player->isInsideShop)
			player->update(&gameMainVector[player->worldX][player->worldY]);
		else player->update(&secretRoomVector[player->worldX][player->worldY]);
		player->draw(mainWindow);*/
		mainWindow.display();
}
void WorldMap::movePlayerToDifferentRoomVector(int oldWorldX, int oldWorldY, int newWorldX, int newWorldY) {

	if(player->isInsideShop){
		secretRoomVector[newWorldX][newWorldY].push_back(player);
		for(int i = 0; i < gameMainVector[oldWorldX][oldWorldY].size(); i++){
			std::shared_ptr<GameObject> tmp = gameMainVector[oldWorldX][oldWorldY].at(i);
			if(tmp == player){
				tmp.reset();
				gameMainVector[oldWorldX][oldWorldY].erase(gameMainVector[oldWorldX][oldWorldY].begin() + i);
			}
		}
	}
	if(!player->isInsideShop){
		//check if the player previous room was a secret room and delete it from the room vector objects
		for(int i = 0; i < secretRoomVector[oldWorldX][oldWorldY].size(); i++){
			std::shared_ptr<GameObject> tmp = secretRoomVector[oldWorldX][oldWorldY].at(i);
			if(tmp == player){
				tmp.reset();
				secretRoomVector[oldWorldX][oldWorldY].erase(secretRoomVector[oldWorldX][oldWorldY].begin() + i);
				gameMainVector[newWorldX][newWorldY].push_back(player);
			}
		}
		//both previous and current room could be non-secret room so do the same as above.
		for(int i = 0; i < gameMainVector[oldWorldX][oldWorldY].size(); i++){
			std::shared_ptr<GameObject> tmp = gameMainVector[oldWorldX][oldWorldY].at(i);
			if(tmp == player){
				tmp.reset();
				gameMainVector[oldWorldX][oldWorldY].erase(gameMainVector[oldWorldX][oldWorldY].begin() + i);
				gameMainVector[newWorldX][newWorldY].push_back(player);
			}
		}
	}
	player->movePlayerToNewVector = false;
}
void WorldMap::drawScreen(sf::RenderWindow& mainWindow, std::vector<std::shared_ptr<GameObject>>* Maplayer) {
	for(auto& obj: *Maplayer)
	{
			obj->draw(mainWindow);
	}
}
void WorldMap::freeSpace(tripleVector&  objVector) {

		for(auto& del : Static::toDelete)
		{
			for(int i = 0; i < objVector[player->worldX][player->worldY].size(); i++){
				std::shared_ptr<GameObject> tmp = objVector[player->worldX][player->worldY].at(i);
				if(tmp == del){
					if(dynamic_cast<MovingSword*>(del.get()))
						player->movingSwordIsActive = false;
					del.reset();
					objVector[player->worldX][player->worldY].erase(objVector[player->worldX][player->worldY].begin() + i);
				}
			}
		}
		Static::toDelete.clear();
}
void WorldMap::addToGameVector(std::vector<std::shared_ptr<GameObject>>* roomObjVector) {
	for(auto& add : Static::toAdd)
	{
		roomObjVector->push_back(add);
	}
	Static::toAdd.clear();
}
void WorldMap::drawAndUpdateCurrentScreen(sf::RenderWindow& mainWindow){
	if(!player->isInsideShop){
		if(player->movePlayerToNewVector)
			movePlayerToDifferentRoomVector(player->prevWorldX, player->prevWorldY, player->worldX, player->worldY);
		freeSpace(gameMainVector);
		drawScreen(mainWindow, &gameBackgroundVector[player->worldX][player->worldY]);
		for(auto& obj : gameMainVector[player->worldX][player->worldY])
		{
			obj->update(&gameMainVector[player->worldX][player->worldY]);
			obj->draw(mainWindow);
		}
		addToGameVector(&gameMainVector[player->worldX][player->worldY]);
	}
	else
	{
		if(player->movePlayerToNewVector)
			movePlayerToDifferentRoomVector(player->prevWorldX, player->prevWorldY, player->worldX, player->worldY);
		freeSpace(secretRoomVector);
		drawScreen(mainWindow, &secretRoomBackgroundVector[player->worldX][player->worldY]);
		for(auto& obj : secretRoomVector[player->worldX][player->worldY])
		{
			obj->update(&secretRoomVector[player->worldX][player->worldY]);
			obj->draw(mainWindow);
		}
		addToGameVector(&secretRoomVector[player->worldX][player->worldY]);
	}
}
void WorldMap::drawRightScreen(sf::RenderWindow& mainWindow){
	if (player->worldY== Global::WorldRoomWidth-1)return;
	drawScreen(mainWindow, &gameBackgroundVector[player->worldX][player->worldY+1]);
	drawScreen(mainWindow, &gameMainVector[player->worldX][player->worldY+1]);
}
void WorldMap::drawLeftScreen(sf::RenderWindow& mainWindow){
	if (player->worldY== 0)return;
	drawScreen(mainWindow, &gameBackgroundVector[player->worldX][player->worldY-1]);
	drawScreen(mainWindow, &gameMainVector[player->worldX][player->worldY -1]);
}
void WorldMap::drawUpScreen(sf::RenderWindow& mainWindow){
	if (player->worldX == 0)return;
	drawScreen(mainWindow, &gameBackgroundVector[player->worldX-1][player->worldY]);
	drawScreen(mainWindow, &gameMainVector[player->worldX-1][player->worldY]);
}
void WorldMap::drawDownScreen(sf::RenderWindow& mainWindow){
	if (player->worldX == Global::WorldRoomHeight-1)return;
	drawScreen(mainWindow, &gameBackgroundVector[player->worldX+1][player->worldY]);
	drawScreen(mainWindow, &gameMainVector[player->worldX + 1][player->worldY]);
}