#include "Misc\WallMasterSpawnController.h"
#include "Utility\Static.h"
#include "Monster\WallMaster.h"
#include "Monster\WallMasterSpawner.h"
WallMasterSpawnController::WallMasterSpawnController(Point pos){
	position = pos;
	hasSpawned = false;
}
void WallMasterSpawnController::update(std::vector<std::shared_ptr<GameObject>>* worldMap){
	WallMasterSpawner* spawn = (WallMasterSpawner*)findClosestSpawner(worldMap).get();
	if (!hasSpawned){
		hasSpawned = true;
		Point spawnPos(spawn->position.x, spawn->position.y);
		std::shared_ptr<GameObject> temp = std::make_shared<WallMaster>(spawnPos, spawn->dir);
		Static::toAdd.push_back(temp);
	}
}
void WallMasterSpawnController::draw(sf::RenderWindow& mainWindow){}