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
		worldLayer1.push_back(tile);
		break;
	case 2:
		tile = new Tile(x, y + Global::inventoryHeight, true, 2);
		worldLayer2.push_back(tile);
		break;
	case 3:
		tile = new Octorok(x, y + Global::inventoryHeight, false);
		worldLayer2.push_back(tile);
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
		drawAndUpdateCurrentScreen(mainWindow);
		drawRightScreen(mainWindow);
		drawLeftScreen(mainWindow);
		drawUpScreen(mainWindow);
		drawDownScreen(mainWindow);
		player->update(worldLayer2);
		player->draw(mainWindow);
		mainWindow.display();
}
void WorldMap::drawScreen(sf::RenderWindow& mainWindow,float minX,float minY,float maxX,float maxY,std::vector<GameObject*> Maplayer){
	for each (GameObject* obj in Maplayer)
	{
		if (obj->xPosition >= minX && obj->xPosition <= maxX && obj->yPosition >= minY && obj->yPosition <= maxY){
			obj->draw(mainWindow);
		}
	}
}
void WorldMap::freeSpace(){
	for each (GameObject* del in toDelete)
	{
		for (int i = 0; i < worldLayer2.size(); i++){
			if (worldLayer2.at(i) == del){
				delete worldLayer2.at(i);
				worldLayer2.erase(worldLayer2.begin()+i);
			}
		}
	}
	toDelete.clear();
}
void WorldMap::drawAndUpdateCurrentScreen(sf::RenderWindow& mainWindow){
	freeSpace();
	float minY = player->worldX*Global::roomHeight;
	float minX = player->worldY*Global::roomWidth;
	float maxY = minY + Global::roomHeight +Global::inventoryHeight;
	float maxX = minX +Global::roomWidth;
	drawScreen(mainWindow, minX, minY, maxX, maxY, worldLayer1);
	for each (GameObject* obj in worldLayer2)
	{
		if (obj->xPosition >= minX && obj->xPosition <= maxX && obj->yPosition >= minY && obj->yPosition <= maxY){
			obj->update(worldLayer2);
			obj->draw(mainWindow);
			if (obj->toBeDeleted)
				toDelete.push_back(obj);
		}
	}
}
void WorldMap::drawRightScreen(sf::RenderWindow& mainWindow){
	float minY = player->worldX*Global::roomHeight;
	float minX = (player->worldY + 1)*Global::roomWidth;
	float maxY = minY + Global::roomHeight + Global::inventoryHeight;
	float maxX = minX + Global::roomWidth;
	if (player->worldY== Global::WorldRoomWidth-1)return;
	drawScreen(mainWindow, minX, minY, maxX, maxY, worldLayer1);
	drawScreen(mainWindow, minX, minY, maxX, maxY, worldLayer2);
}
void WorldMap::drawLeftScreen(sf::RenderWindow& mainWindow){
	float minY = player->worldX*Global::roomHeight;
	float minX = (player->worldY - 1)*Global::roomWidth;
	float maxY = minY + Global::roomHeight + Global::inventoryHeight;
	float maxX = minX + Global::roomWidth;

	if (player->worldY== 0)return;
	drawScreen(mainWindow, minX, minY, maxX, maxY, worldLayer1);
	drawScreen(mainWindow, minX, minY, maxX, maxY, worldLayer2);
}
void WorldMap::drawUpScreen(sf::RenderWindow& mainWindow){
	float minY = (player->worldX - 1)*Global::roomHeight;
	float minX = player->worldY*Global::roomWidth;
	float maxY = minY + Global::roomHeight + Global::inventoryHeight;
	float maxX = minX + Global::roomWidth;

	if (player->worldX == 0)return;
	drawScreen(mainWindow, minX, minY, maxX, maxY, worldLayer1);
	drawScreen(mainWindow, minX, minY, maxX, maxY, worldLayer2);
}
void WorldMap::drawDownScreen(sf::RenderWindow& mainWindow){
	float minY = (player->worldX + 1)*Global::roomHeight;
	float minX = player->worldY*Global::roomWidth;
	float maxY = minY + Global::roomHeight + Global::inventoryHeight;
	float maxX = minX + Global::roomWidth;
	if (player->worldX == Global::WorldRoomHeight-1)return;
	drawScreen(mainWindow, minX, minY, maxX, maxY, worldLayer1);
	drawScreen(mainWindow, minX, minY, maxX, maxY, worldLayer2);
}