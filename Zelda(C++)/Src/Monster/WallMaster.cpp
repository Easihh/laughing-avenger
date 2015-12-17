#include "Monster\WallMaster.h"
#include "Utility\Static.h"
#include "Monster\DeathEffect.h"
#include "Misc\Tile.h"
#include "Type\RupeeType.h"
#include "Item\RupeeDrop.h"
#include "Item\ThrownBoomrang.h"
#include "Player\Player.h"
#include "Monster\WallMasterSpawner.h"
WallMaster::WallMaster(Point pos,Direction spawnDir){
	position =lastSpawnLocation= pos;
	depth = 1000;
	width = Global::TileWidth;
	height = Global::TileHeight;
	loadAnimation();
	isInvincible = false;
	healthPoint = 20;
	strength = 1;
	currentInvincibleFrame = 0;
	pushbackStep = 0;
	setupFullMask();
	setupMonsterMask();
	dir =spawnedDir= spawnDir;
	walkAnimIndex = 0;
	isParalyzed = false;
	currentParalyzeTime = 0;
	hasCaughtPlayer = false;
}
void WallMaster::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(walkingAnimation[walkAnimIndex]->sprite);
	//mainWindow.draw(*mask);
	//mainWindow.draw(*fullMask);
}
void WallMaster::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	setDirection(worldMap);
	checkCollisions(worldMap);
	checkParalyzeStatus();
	if (hasCaughtPlayer)
		movePlayerToDungeonEntrance(worldMap);
	if (pushbackStep == 0 && !isParalyzed)
		movement(worldMap);
	else if (pushbackStep != 0 && !isParalyzed)
		pushbackUpdate();
	updateAnimation();
	if (healthPoint <= 0)
		processDeath(worldMap);
	else checkInvincibility();
	updateMasks();
}
void WallMaster::checkCollisions(std::vector<std::shared_ptr<GameObject>>* worldMap){
	if (isCollidingWithPlayer(worldMap) && !hasCaughtPlayer){
		Player* temp = (Player*)findPlayer(worldMap).get();
		if (!temp->inputIsDisabled){//player isnt caught already by another WallMaster
			hasCaughtPlayer = true;
			Sound::playSound(GameSound::TakeDamage);
			temp->position.x = position.x;
			temp->position.y = position.y;
			temp->inputIsDisabled = true;
			//used to determine which room player was before the teleport to entrance.
			int prevWorldX = position.y / Global::roomHeight;
			int prevWorldY = position.x / Global::roomWidth;
			temp->prevWorldX = prevWorldX;
			temp->prevWorldY = prevWorldY;
		}
	}
	if (isCollidingWithBoomerang(worldMap)){
		Sound::playSound(GameSound::EnemyHit);
		isParalyzed = true;
		ThrownBoomrang* boom = (ThrownBoomrang*)findBoomerang(worldMap).get();
		if (!boom->isReturning)
			boom->isReturning = true;
	}
}
void WallMaster::updateAnimation(){
	for (int i = 0; i < 3; i++)
		walkingAnimation[i]->updateAnimationFrame(position);
}
void WallMaster::processDeath(std::vector<std::shared_ptr<GameObject>>* worldMap){
	Point pt(position.x + (width / 4), position.y + (height / 4));
	std::shared_ptr<GameObject> add = std::make_shared<DeathEffect>(pt);
	Static::toAdd.push_back(add);
	Sound::playSound(GameSound::SoundType::EnemyKill);
	dropItemOnDeath();
	destroyGameObject(worldMap);
}
void WallMaster::movePlayerToDungeonEntrance(std::vector<std::shared_ptr<GameObject>>* worldMap){
	Player* temp = (Player*)findPlayer(worldMap).get();
	switch (dir){
	case Direction::Left:
		temp->position.x -= minStep;
		break;
	case Direction::Right:
		temp->position.x += minStep;
		break;
	case Direction::Up:
		temp->position.y -= minStep;
		break;
	case Direction::Down:
		temp->position.y += minStep;
		break;
	}
	for (int i = 0; i < 3; i++)
		temp->walkingAnimation[i]->updateAnimationFrame(temp->dir, temp->position);
}
void WallMaster::dropItemOnDeath() {
	bool willDropItem = false;
	int value = std::rand() % 10;
	if (value < 3) //30% chance to drop item
		willDropItem = true;
	if (willDropItem){
		int itemDropId = std::rand() % 2;
		std::shared_ptr<GameObject> itemDropped;
		switch (itemDropId){
		case 0:
			itemDropped = std::make_shared<RupeeDrop>(position, RupeeType::OrangeRupee);
			break;
		case 1:
			itemDropped = std::make_shared<RupeeDrop>(position, RupeeType::BlueRupee);
			break;
		}
		Static::toAdd.push_back(itemDropped);
	}
}
void WallMaster::takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction attackDir) {
	if (!isInvincible){
		healthPoint -= damage;
		if (healthPoint >= 1){
			Sound::playSound(GameSound::EnemyHit);
			if (pushbackStep == 0)
				pushBack(worldMap, attackDir);
		}
		isInvincible = true;
		walkAnimIndex = 1;
	}
}
void WallMaster::takeDamage(int damage){
	if (!isInvincible){
		if (healthPoint > damage)
			Sound::playSound(GameSound::EnemyHit);
		healthPoint -= damage;
		isInvincible = true;
		walkAnimIndex = 1;
	}
}
int WallMaster::getXOffset(){
	if (dir == Direction::Left)
		return -minStep;
	else if (dir == Direction::Right)
		return minStep;
	else return 0;
}
int WallMaster::getYOffset(){
	if (dir == Direction::Up)
		return -minStep;
	else if (dir == Direction::Down)
		return minStep;
	else return 0;
}
void WallMaster::setMaxDistance(Direction spawnDir){
	switch (spawnDir){
	case Direction::Down:
		maxXDistance = 4*Global::TileWidth;
		maxYDistance = 2*Global::TileHeight;
		break;
	case Direction::Right:
		maxXDistance = 2*Global::TileWidth;
		maxYDistance = 4*Global::TileHeight;
		break;
	case Direction::Left:
		maxXDistance = 2*Global::TileWidth;
		maxYDistance = 4 * Global::TileHeight;
		break;
	case Direction::Up:
		maxXDistance = 4 * Global::TileWidth;
		maxYDistance = 2*Global::TileHeight;
		break;
	}
}
void WallMaster::setDirection(std::vector<std::shared_ptr<GameObject>>* worldMap){
	Player* player = (Player*)findPlayer(worldMap).get();
	WallMasterSpawner* nearPlayer = (WallMasterSpawner*)findClosestSpawner(worldMap, player->position).get();
	setMaxDistance(spawnedDir);
	float distanceX = std::abs(position.x - lastSpawnLocation.x);
	float distanceY = std::abs(position.y - lastSpawnLocation.y);
	switch (spawnedDir){
	case Direction::Down:
		if (distanceX == 0 && distanceY == 0){
			position = nearPlayer->position;
			dir=spawnedDir = nearPlayer->dir;
			lastSpawnLocation = position;
		}
		else if (distanceY==maxYDistance && distanceX==0)
			dir = Direction::Left;
		else if (distanceX >= maxXDistance && distanceY == maxYDistance)
			dir = Direction::Up;
		else if (distanceX >= maxXDistance && distanceY == 0){
			dir = Direction::Right;
			if (player->inputIsDisabled){//player was caught
				player->movePlayerToDungeonEntrance();
				hasCaughtPlayer = false;
			}
		}
		break;
	case Direction::Right:
		if (distanceX == 0 && distanceY == 0){
			position = nearPlayer->position;
			dir = spawnedDir = nearPlayer->dir;
			lastSpawnLocation = position;
		}
		else if (distanceX >= maxXDistance && distanceY<128)
			dir = Direction::Up;
		else if (distanceY >= maxYDistance && distanceX > 0)
			dir = Direction::Left;
		else if (distanceY == maxYDistance && distanceX == 0){
			dir = Direction::Down;
			if (player->inputIsDisabled){//player was caught
				player->movePlayerToDungeonEntrance();
				hasCaughtPlayer = false;
			}
		}
		break;
	case Direction::Left:
		if (distanceX == 0 && distanceY == 0){
			position = nearPlayer->position;
			dir = spawnedDir = nearPlayer->dir;
			lastSpawnLocation = position;
		}
		else if (distanceX == maxXDistance && distanceY<128)
			dir = Direction::Up;
		else if (distanceY >= maxYDistance && distanceX > 0){
			dir = Direction::Right;
		}
		else if (distanceY >= maxYDistance && distanceX == 0){
			dir = Direction::Down;
			if (player->inputIsDisabled){//player was caught
				player->movePlayerToDungeonEntrance();
				hasCaughtPlayer = false;
			}
		}
		break;
	case Direction::Up:
		if (distanceX == 0 && distanceY == 0){
			position = nearPlayer->position;
			dir = spawnedDir = nearPlayer->dir;
			lastSpawnLocation = position;
		}
		else if (distanceY == maxYDistance && distanceX == 0){
			dir = Direction::Left;
		}
		else if (distanceX >= maxXDistance && distanceY == maxYDistance)
			dir = Direction::Down;
		else if (distanceX >= maxXDistance && distanceY == 0){
			dir = Direction::Right;
			if (player->inputIsDisabled){//player was caught
				player->movePlayerToDungeonEntrance();
				hasCaughtPlayer = false;
			}
		}
		break;
	}
}
void WallMaster::movement(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	switch (dir){
	case Direction::Down:
		position.y += minStep;
		break;
	case Direction::Up:
		position.y -= minStep;
		break;
	case Direction::Left:
		position.x -= minStep;
		break;
	case Direction::Right:
		position.x += minStep;
		break;
	}
}
void WallMaster::loadAnimation(){
	walkingAnimation.push_back(std::make_unique<Animation>("WallMaster", height, width, position, 8));
	walkingAnimation.push_back(std::make_unique<Animation>("WallMaster_Hit1", height, width, position, 8));
	walkingAnimation.push_back(std::make_unique<Animation>("WallMaster_Hit2", height, width, position, 8));
}
