#include "Misc\WallMasterSpawnController.h"
#include "Utility\Static.h"
#include "Monster\WallMaster.h"
#include "Monster\WallMasterSpawner.h"
#include "Player\Player.h"
WallMasterSpawnController::WallMasterSpawnController(Point pos){
	position = pos;
	currentSpawnCount = 9;
	currentTimeSpawner = 0;
}
void WallMasterSpawnController::update(std::vector<std::shared_ptr<GameObject>>* worldMap){
	currentTimeSpawner++;
	if (currentTimeSpawner>maxTimeSpawner && currentSpawnCount<maxSpawn){
		Player* player = (Player*)findPlayer(worldMap).get();
		WallMasterSpawner* spawn = (WallMasterSpawner*)findClosestSpawner(worldMap, player->position).get();
		currentTimeSpawner = 0;
		Point spawnPos(spawn->position.x, spawn->position.y);
		std::shared_ptr<GameObject> temp = std::make_shared<WallMaster>(spawnPos, spawn->dir);
		Static::toAdd.push_back(temp);
		currentSpawnCount++;
	}
}
void WallMasterSpawnController::draw(sf::RenderWindow& mainWindow){}