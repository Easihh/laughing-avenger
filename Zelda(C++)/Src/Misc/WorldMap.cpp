#include "Misc\WorldMap.h"
#include "Misc\Tile.h"
#include "Item\ThrownArrow.h"
#include "Item\ThrownBomb.h"
#include "Item\BombEffect.h"
#include "Misc\ShopObject.h"
#include "Misc\CandleFlame.h"
#include "Type\Identifier.h"
#include "Item\ThrownBoomrang.h"
#include "Misc\MoveableBlock.h"
#include "Item\HeartDrop.h"
#include "Item\RupeeDrop.h"
WorldMap::WorldMap(){
	setupVectors();
	parser = std::make_unique<TileParser>();
	std::clock_t start;
	start = std::clock();
	double duration;
	loadMap("Map/Zelda-Worldmap_Layer 1.csv", gameBackgroundVector);
	loadMap("Map/Zelda-Worldmap_Layer 2.csv", gameMainVector);
	loadMap("Map/Zelda-Shop_Layer 1.csv", secretRoomBackgroundVector);
	loadMap("Map/Zelda-Shop_Layer 2.csv", secretRoomVector);
	loadMap("Map/Zelda-Dungeon_Layer 1.csv", dungeonBackgroundVector);
	loadMap("Map/Zelda-Dungeon_Layer 2.csv", dungeonVector);
	sort(gameMainVector);
	sort(secretRoomVector);
	sort(dungeonVector);
	duration = (std::clock() - start) / (double)1000;
	std::cout << "printf:" << duration << '\n';
}
bool sortByDepth(std::shared_ptr<GameObject> object1, std::shared_ptr<GameObject> object2) {
	return object1.get()->depth<object2.get()->depth;
}
void WorldMap::sort(tripleVector& objectVector) {
	for(int i = 0; i < Global::WorldRoomWidth; i++){
		for(int j = 0; j < Global::WorldRoomHeight; j++){
			if(objectVector[i][j].size() >0)
				std::sort(objectVector[i][j].begin(), objectVector[i][j].end(),sortByDepth);
		}
	}
}
void WorldMap::sort(std::vector<std::shared_ptr<GameObject>>* roomObjVector) {
	for (int i = 0; i < roomObjVector->size(); i++){
		std::sort(roomObjVector->begin(), roomObjVector->end(), sortByDepth);
	}
}
void WorldMap::setupVectors() {
	/*pre populate vector of vectors with empty vectors*/
	for(int i = 0; i < Global::WorldRoomWidth; i++){
		mainBackgroundColumns.push_back(roomBackGroundTile);
		secretRoomBackgroundColumns.push_back(secretRoomTile);
		mainVectorColums.push_back(roomGameObjects);
		secretRoomColumns.push_back(secretRoomGameObjects);
		dungeonColumns.push_back(dungeonGameObjects);
		dungeonBackgroundColumns.push_back(dungeonTile);
	}
	for(int i = 0; i < Global::WorldRoomHeight; i++){
		gameMainVector.push_back(mainVectorColums);
		secretRoomVector.push_back(secretRoomColumns);
		gameBackgroundVector.push_back(mainBackgroundColumns);
		secretRoomBackgroundVector.push_back(secretRoomBackgroundColumns);
		dungeonVector.push_back(dungeonColumns);
		dungeonBackgroundVector.push_back(dungeonBackgroundColumns);
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
	int elementCount = 0;
	int currentIndex;
	std::string idString;
	int minSubstring;
	int parsedId;
	int counter;
	if (in.fail())
		std::cout << "Failed To open:" + filename<<std::endl;
	while (!in.eof()){
		lastWorldXIndex = 0;
		minSubstring = 0;
		currentIndex = 0;
		counter = 0;
		idString = "";
		std::getline(in, line, '\n');
		bool isRunning = true;
		while (isRunning){
			while (currentIndex<line.size() && line.at(currentIndex) != ','){
				currentIndex++;
				counter++;
			}
			idString = line.substr(minSubstring, counter);
			currentIndex++;//pass over , char
			minSubstring = currentIndex;
			if (minSubstring >= line.size())
				isRunning = false;
			counter = 0;
			parsedId = atoi(idString.c_str());
			elementCount++;

			createTile(lastWorldXIndex, lastWorldYIndex, parsedId, objectVector);
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
	}
	in.close();
}
void WorldMap::createTile(int lastWorldXIndex, int lastWorldYIndex, int tileType, tripleVector& objectVector) {
	float x = lastWorldXIndex*Global::TileWidth;
	float y = lastWorldYIndex*Global::TileHeight;
	Point pt(x, y + Global::inventoryHeight);
	switch(tileType){
	case -1:
		//no tile;
		break;
	case Identifier::Player_ID:
		player = std::make_shared<Player>(pt);
		objectVector[vectorXindex][vectorYindex].push_back(player);
		break;
	default:
		parser->createTile(lastWorldXIndex, lastWorldYIndex, tileType, objectVector, vectorXindex, vectorYindex);
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
		mainWindow.display();
}
void WorldMap::enableShopObjects(std::vector<std::shared_ptr<GameObject>>* roomObjVector) {
	for(int i = 0; i < roomObjVector->size(); i++){
		if(dynamic_cast<ShopObject*>(roomObjVector->at(i).get())){
			ShopObject* tmp = ((ShopObject*)roomObjVector->at(i).get());
			tmp->isVisible = true;
		}

	}
}
void WorldMap::movePlayerToDifferentRoomVector(int oldWorldX, int oldWorldY, int newWorldX, int newWorldY) {
	player->movingSwordIsActive = false;
	player->boomerangIsActive = false;
	player->arrowIsActive = false;
	Sound::stopSound(GameSound::Boomerang);
	if(player->currentLayer == InsideShop){
		secretRoomVector[newWorldX][newWorldY].push_back(player);
		sort(secretRoomVector);
		enableShopObjects(&secretRoomVector[newWorldX][newWorldY]);
		for(int i = 0; i < gameMainVector[oldWorldX][oldWorldY].size(); i++){
			std::shared_ptr<GameObject> tmp = gameMainVector[oldWorldX][oldWorldY].at(i);
			if(tmp == player){
				tmp.reset();
				gameMainVector[oldWorldX][oldWorldY].erase(gameMainVector[oldWorldX][oldWorldY].begin() + i);
				deleteOutstandingPlayerObjects(&gameMainVector[oldWorldX][oldWorldY]);
			}
		}
	}
	if(player->currentLayer == OverWorld){
		//check if the player previous room was a secret room and delete it from the room vector objects
		if(player->prevLayer == Layer::InsideShop){
			for(int i = 0; i < secretRoomVector[oldWorldX][oldWorldY].size(); i++){
				std::shared_ptr<GameObject> tmp = secretRoomVector[oldWorldX][oldWorldY].at(i);
				if(tmp == player){
					tmp.reset();
					secretRoomVector[oldWorldX][oldWorldY].erase(secretRoomVector[oldWorldX][oldWorldY].begin() + i);
					gameMainVector[newWorldX][newWorldY].push_back(player);
					//sort(gameMainVector);
					deleteOutstandingPlayerObjects(&secretRoomVector[oldWorldX][oldWorldY]);
				}
			}
		}
		//both previous and current room could be non-secret room so do the same as above.
		if(player->prevLayer == Layer::OverWorld){
			for(int i = 0; i < gameMainVector[oldWorldX][oldWorldY].size(); i++){
				std::shared_ptr<GameObject> tmp = gameMainVector[oldWorldX][oldWorldY].at(i);
				if(tmp == player){
					tmp.reset();
					gameMainVector[oldWorldX][oldWorldY].erase(gameMainVector[oldWorldX][oldWorldY].begin() + i);
					gameMainVector[newWorldX][newWorldY].push_back(player);
					sort(gameMainVector);
					deleteOutstandingPlayerObjects(&gameMainVector[oldWorldX][oldWorldY]);
				}
			}
		}
		if(player->prevLayer == Layer::Dungeon){
			for(int i = 0; i < dungeonVector[oldWorldX][oldWorldY].size(); i++){
				std::shared_ptr<GameObject> tmp = dungeonVector[oldWorldX][oldWorldY].at(i);
				if(tmp == player){
					tmp.reset();
					dungeonVector[oldWorldX][oldWorldY].erase(dungeonVector[oldWorldX][oldWorldY].begin() + i);
					gameMainVector[newWorldX][newWorldY].push_back(player);
					sort(gameMainVector);
					deleteOutstandingPlayerObjects(&dungeonVector[oldWorldX][oldWorldY]);
				}
			}
		}
	}
	if(player->currentLayer == Dungeon){
		if(player->prevLayer == OverWorld){
			dungeonVector[newWorldX][newWorldY].push_back(player);
			sort(dungeonVector);
			for(int i = 0; i < gameMainVector[oldWorldX][oldWorldY].size(); i++){
				std::shared_ptr<GameObject> tmp = gameMainVector[oldWorldX][oldWorldY].at(i);
				if(tmp == player){
					tmp.reset();
					gameMainVector[oldWorldX][oldWorldY].erase(gameMainVector[oldWorldX][oldWorldY].begin() + i);
					deleteOutstandingPlayerObjects(&gameMainVector[oldWorldX][oldWorldY]);
				}
			}
		}
		if(player->prevLayer == Dungeon){
			dungeonVector[newWorldX][newWorldY].push_back(player);
			sort(dungeonVector);
			for(int i = 0; i < dungeonVector[oldWorldX][oldWorldY].size(); i++){
				std::shared_ptr<GameObject> tmp = dungeonVector[oldWorldX][oldWorldY].at(i);
				if(tmp == player){
					tmp.reset();
					dungeonVector[oldWorldX][oldWorldY].erase(dungeonVector[oldWorldX][oldWorldY].begin() + i);
					deleteOutstandingPlayerObjects(&dungeonVector[oldWorldX][oldWorldY]);
				}
				if (dynamic_cast<MoveableBlock*>(tmp.get())){
					MoveableBlock* block = (MoveableBlock*)tmp.get();
					block->resetPosition();
				}
			}
		}
	}
	player->movePlayerToNewVector = false;
}
void WorldMap::deleteOutstandingPlayerObjects(std::vector<std::shared_ptr<GameObject>>* roomObjVector) {
	//used to delete outstanding objects when player move from one room to another such as moving sword,bomb etc
	int index = 0;	
	for(int i = 0; i < roomObjVector->size(); i++){
		std::shared_ptr<GameObject> tmp = roomObjVector->at(i);
		if(dynamic_cast<MovingSword*>(tmp.get())
			|| dynamic_cast<ThrownBomb*>(tmp.get())
			|| dynamic_cast<ThrownArrow*>(tmp.get())
			|| dynamic_cast<BombEffect*>(tmp.get())
			|| dynamic_cast<CandleFlame*>(tmp.get())
			|| dynamic_cast<ThrownBoomrang*>(tmp.get())
			|| dynamic_cast<RupeeDrop*>(tmp.get())
			|| dynamic_cast<HeartDrop*>(tmp.get())
			){
			tmp.reset();
			roomObjVector->erase(roomObjVector->begin() + i);
			i--;
		}
	}
}
void WorldMap::drawScreen(sf::RenderWindow& mainWindow, std::vector<std::shared_ptr<GameObject>>* Maplayer) {
	for(auto& obj: *Maplayer)
	{
			obj->draw(mainWindow);
	}
}
void WorldMap::freeSpace(tripleVector&  objVector,int worldX,int worldY) {

		for (int j = 0; j < Static::toDelete.size();j++)
		{
			std::shared_ptr<GameObject> del = Static::toDelete.at(j);
			for(int i = 0; i < objVector[worldX][worldY].size(); i++){
				std::shared_ptr<GameObject> tmp = objVector[worldX][worldY].at(i);
				if(tmp == del){
					if(dynamic_cast<MovingSword*>(del.get()))
						player->movingSwordIsActive = false;
					del.reset();
					objVector[worldX][worldY].erase(objVector[worldX][worldY].begin() + i);
					Static::toDelete.erase(Static::toDelete.begin()+j);
					j--;
				}
			}
		}
}
void WorldMap::addToGameVector(std::vector<std::shared_ptr<GameObject>>* roomObjVector) {
	for(auto& add : Static::toAdd)
	{
		roomObjVector->push_back(add);
	}
	sort(roomObjVector);
	Static::toAdd.clear();
}
void WorldMap::drawAndUpdateCurrentScreen(sf::RenderWindow& mainWindow){
	if (Static::toDelete.size() > 0){
		freeSpace(gameMainVector,player->worldX,player->worldY);
		freeSpace(dungeonVector, player->worldX, player->worldY);
		freeSpace(secretRoomVector, player->worldX, player->worldY);
		//used when teleporting from one layer point to another layer differnet point such as triforce.
		freeSpace(gameMainVector, player->prevWorldX, player->prevWorldY);
		freeSpace(dungeonVector, player->prevWorldX, player->prevWorldY);
		freeSpace(secretRoomVector, player->prevWorldX, player->prevWorldY);
	}
	if(player->currentLayer==OverWorld){
		if(player->movePlayerToNewVector)
			movePlayerToDifferentRoomVector(player->prevWorldX, player->prevWorldY, player->worldX, player->worldY);
		drawScreen(mainWindow, &gameBackgroundVector[player->worldX][player->worldY]);
		for(auto& obj : gameMainVector[player->worldX][player->worldY])
		{
			if (!player->movePlayerToNewVector){
				//if true the current vector may have changed mid loop since worldX/worldY 
				//may be udpated(Teleport to Artifact room for example)
				//->unpredictable errors in other object update from previous vector.
				obj->update(&gameMainVector[player->worldX][player->worldY]);
				obj->draw(mainWindow);
			}
		}
		if (Static::toAdd.size()>0)
			addToGameVector(&gameMainVector[player->worldX][player->worldY]);
	}
	else if(player->currentLayer == InsideShop)
	{
		if(player->movePlayerToNewVector)
			movePlayerToDifferentRoomVector(player->prevWorldX, player->prevWorldY, player->worldX, player->worldY);
		drawScreen(mainWindow, &secretRoomBackgroundVector[player->worldX][player->worldY]);
		for(auto& obj : secretRoomVector[player->worldX][player->worldY])
		{
			if (!player->movePlayerToNewVector){
				obj->update(&secretRoomVector[player->worldX][player->worldY]);
				obj->draw(mainWindow);
			}
		}
		if (Static::toAdd.size()>0)
			addToGameVector(&secretRoomVector[player->worldX][player->worldY]);
	}
	else if(player->currentLayer == Dungeon)
	{
		if(player->movePlayerToNewVector)
			movePlayerToDifferentRoomVector(player->prevWorldX, player->prevWorldY, player->worldX, player->worldY);
		drawScreen(mainWindow, &dungeonBackgroundVector[player->worldX][player->worldY]);
		for(auto& obj : dungeonVector[player->worldX][player->worldY])
		{
			if (!player->movePlayerToNewVector){
				obj->update(&dungeonVector[player->worldX][player->worldY]);
				obj->draw(mainWindow);
			}
		}
		if (Static::toAdd.size()>0)
			addToGameVector(&dungeonVector[player->worldX][player->worldY]);
	}
}
void WorldMap::drawRightScreen(sf::RenderWindow& mainWindow){
	if(player->worldY == Global::WorldRoomWidth - 1)return;
	if(player->currentLayer == OverWorld){
		drawScreen(mainWindow, &gameBackgroundVector[player->worldX][player->worldY + 1]);
		drawScreen(mainWindow, &gameMainVector[player->worldX][player->worldY + 1]);
	}
	else if(player->currentLayer == InsideShop){
		drawScreen(mainWindow, &secretRoomBackgroundVector[player->worldX][player->worldY + 1]);
		drawScreen(mainWindow, &secretRoomVector[player->worldX][player->worldY + 1]);
	}
	else
	{
		drawScreen(mainWindow, &dungeonBackgroundVector[player->worldX][player->worldY + 1]);
		drawScreen(mainWindow, &dungeonVector[player->worldX][player->worldY + 1]);
	}
}
void WorldMap::drawLeftScreen(sf::RenderWindow& mainWindow){
	if(player->worldY == 0)return;
	if(player->currentLayer == OverWorld){
		drawScreen(mainWindow, &gameBackgroundVector[player->worldX][player->worldY - 1]);
		drawScreen(mainWindow, &gameMainVector[player->worldX][player->worldY - 1]);
	}
	else if(player->currentLayer == InsideShop){
		drawScreen(mainWindow, &secretRoomBackgroundVector[player->worldX][player->worldY - 1]);
		drawScreen(mainWindow, &secretRoomVector[player->worldX][player->worldY - 1]);
	}
	else
	{
		drawScreen(mainWindow, &dungeonBackgroundVector[player->worldX][player->worldY - 1]);
		drawScreen(mainWindow, &dungeonVector[player->worldX][player->worldY - 1]);
	}
}
void WorldMap::drawUpScreen(sf::RenderWindow& mainWindow){
	if(player->worldX == 0)return;
	if(player->currentLayer == OverWorld){
		drawScreen(mainWindow, &gameBackgroundVector[player->worldX - 1][player->worldY]);
		drawScreen(mainWindow, &gameMainVector[player->worldX - 1][player->worldY]);
	}
	else if(player->currentLayer == InsideShop){
		drawScreen(mainWindow, &secretRoomBackgroundVector[player->worldX-1][player->worldY]);
		drawScreen(mainWindow, &secretRoomVector[player->worldX-1][player->worldY]);
	}
	else
	{
		drawScreen(mainWindow, &dungeonBackgroundVector[player->worldX-1][player->worldY]);
		drawScreen(mainWindow, &dungeonVector[player->worldX-1][player->worldY]);
	}
}
void WorldMap::drawDownScreen(sf::RenderWindow& mainWindow){
	if(player->worldX == Global::WorldRoomHeight - 1)return;
	if(player->currentLayer == OverWorld){
		drawScreen(mainWindow, &gameBackgroundVector[player->worldX + 1][player->worldY]);
		drawScreen(mainWindow, &gameMainVector[player->worldX + 1][player->worldY]);
	}
	else if(player->currentLayer == InsideShop){
		drawScreen(mainWindow, &secretRoomBackgroundVector[player->worldX + 1][player->worldY]);
		drawScreen(mainWindow, &secretRoomVector[player->worldX + 1][player->worldY]);
	}
	else
	{
		drawScreen(mainWindow, &dungeonBackgroundVector[player->worldX + 1][player->worldY]);
		drawScreen(mainWindow, &dungeonVector[player->worldX + 1][player->worldY]);
	}
}