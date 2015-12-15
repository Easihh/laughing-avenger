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
	position = pos;
	depth = 999;
	width = Global::TileWidth;
	height = Global::TileHeight;
	loadAnimation();
	isInvincible = false;
	healthPoint = 2;
	strength = 1;
	currentInvincibleFrame = 0;
	pushbackStep = 0;
	setupFullMask();
	setupMonsterMask();
	dir = spawnDir;
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
	if (isCollidingWithPlayer(worldMap)){
		Player* temp = (Player*)findPlayer(worldMap).get();
		hasCaughtPlayer = true;
	}
	checkParalyzeStatus();
	if (isCollidingWithBoomerang(worldMap)){
		Sound::playSound(GameSound::EnemyHit);
		isParalyzed = true;
		ThrownBoomrang* boom = (ThrownBoomrang*)findBoomerang(worldMap).get();
		if (!boom->isReturning)
			boom->isReturning = true;
	}
	if (pushbackStep == 0 && !isParalyzed){
		movement(worldMap);
	}
	else if (pushbackStep != 0 && !isParalyzed)
		pushbackUpdate();
	for (int i = 0; i < 3; i++)
		walkingAnimation[i]->updateAnimationFrame(position);
	if (healthPoint <= 0){
		Point pt(position.x + (width / 4), position.y + (height / 4));
		std::shared_ptr<GameObject> add = std::make_shared<DeathEffect>(pt);
		Static::toAdd.push_back(add);
		destroyGameObject(worldMap);
		Sound::playSound(GameSound::SoundType::EnemyKill);
		dropItemOnDeath();
	}
	else
	{
		checkInvincibility();
	}
	updateMasks();
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
		maxYDistance = Global::TileHeight;
		break;
	case Direction::Right:
		maxXDistance = Global::TileWidth;
		maxYDistance = 4*Global::TileHeight;
		break;
	case Direction::Left:
		maxXDistance = Global::TileWidth;
		maxYDistance = 4 * Global::TileHeight;
		break;
	case Direction::Up:
		maxXDistance = 4 * Global::TileWidth;
		maxYDistance = Global::TileHeight;
		break;
	}
}
void WallMaster::setDirection(std::vector<std::shared_ptr<GameObject>>* worldMap){
	WallMasterSpawner* spawn = (WallMasterSpawner*)findClosestSpawner(worldMap).get();
	setMaxDistance(spawn->dir);
	float distanceX = std::abs(position.x - spawn->position.x);
	float distanceY = std::abs(position.y - spawn->position.y);
	switch (spawn->dir){
	case Direction::Down:
		break;
	case Direction::Right:
		if (distanceX==0 && distanceY == 0)
			dir = Direction::Right;
		else if (distanceX == Global::TileWidth && distanceY<128)
			dir = Direction::Up;
		else if (distanceY == maxYDistance && distanceX>0)
			dir = Direction::Left;
		else if (distanceY == maxYDistance && distanceX == 0)
			dir = Direction::Down;
		break;
	case Direction::Left:
		break;
	case Direction::Up:
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
