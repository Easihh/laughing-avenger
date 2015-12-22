#include "Monster\Aquamentus.h"
#include "Utility\Static.h"
#include <iostream>
#include "Monster\DeathEffect.h"
#include "Misc\Tile.h"
#include "Player\Player.h"
#include "Monster\ZoraProjectile.h"
#include "Item\HeartContainer.h"
Aquamentus::Aquamentus(Point pos){
	position = pos;
	width = 2*Global::TileWidth;
	height = 2*Global::TileHeight;
	loadAnimation();
	isInvincible = false;
	healthPoint = 6;
	strength = 2;
	currentInvincibleFrame = 0;
	pushbackStep = 0;
	setupMask(&fullMask, width, height, sf::Color::Magenta);
	setupMask(&mask, 32, 24, sf::Color::Cyan);
	dir = Direction::Left;
	walkAnimIndex = 0;
	currentForwardDistance = 0;
	timeSinceLastProjectile = 0;
}
void Aquamentus::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(walkingAnimation[walkAnimIndex]->sprite);
	//mainWindow.draw(*mask);
	//mainWindow.draw(*fullMask);
}
void Aquamentus::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	if (isCollidingWithPlayer(worldMap)){
		Player* temp = (Player*)findPlayer(worldMap).get();
		temp->takeDamage(worldMap, this);
	}
	movement(worldMap);
	shoot();
	for (int i = 0; i < 3; i++)
		walkingAnimation[i]->updateAnimationFrame(position);
	if (healthPoint <= 0)
		processDeath(worldMap);
	else checkInvincibility();
	mask->setPosition(position.x+8, position.y);
	fullMask->setPosition(position.x, position.y);
}
void Aquamentus::processDeath(std::vector<std::shared_ptr<GameObject>>* worldMap){
	Player* temp = (Player*)findPlayer(worldMap).get();
	temp->boss1IsAlive = false;
	Point pt(position.x + (width / 4), position.y + (height / 4));
	std::shared_ptr<GameObject> add = std::make_shared<DeathEffect>(pt);
	Static::toAdd.push_back(add);
	destroyGameObject(worldMap);
	Sound::playSound(GameSound::SoundType::EnemyKill);
	Sound::stopSound(GameSound::SoundType::BossScream1);
	dropItemOnDeath();
	openDoor(worldMap);
}
void Aquamentus::openDoor(std::vector<std::shared_ptr<GameObject>>* worldMap){
	Sound::playSound(GameSound::DoorUnlock);
	std::shared_ptr<GameObject> del;
	for (auto& obj : *worldMap){
		if (dynamic_cast<Tile*>(obj.get())){
			Tile* temp = (Tile*)obj.get();
			if (temp->id == (int)TileType::DungeonTile9){
				temp->texture.loadFromFile("Tileset/Dungeon/dungeonTile88.png");
				temp->sprite.setTexture(temp->texture);
				temp->isCollideable = false;
			}
		}
	}
}
void Aquamentus::shoot(){
	timeSinceLastProjectile ++;
	if (timeSinceLastProjectile > maxTimeSinceLastProjectile){
		std::shared_ptr<GameObject> add;
		timeSinceLastProjectile = 0;
		Point pt(mask.get()->getPosition().x, mask.get()->getPosition().y);
		add = std::make_shared<ZoraProjectile>(pt,Direction::Left);
		Static::toAdd.push_back(add);
		pt.setPoint(mask.get()->getPosition().x, mask.get()->getPosition().y-Global::TileHeight);
		add = std::make_shared<ZoraProjectile>(pt, Direction::TopLeft);
		Static::toAdd.push_back(add);
		pt.setPoint(mask.get()->getPosition().x, mask.get()->getPosition().y + Global::TileHeight);
		add = std::make_shared<ZoraProjectile>(pt, Direction::BottomLeft);
		Static::toAdd.push_back(add);
	}
}
void Aquamentus::dropItemOnDeath() {
	Point dropPosition(61* Global::TileWidth, 12* Global::TileHeight);
	std::shared_ptr<GameObject> itemDropped = std::make_shared<HeartContainer>(dropPosition);
	Sound::playSound(GameSound::ItemAppear);
	Static::toAdd.push_back(itemDropped);
}
void Aquamentus::takeDamage(int damage, std::vector<std::shared_ptr<GameObject>>* worldMap, Direction attackDir) {
	if (!isInvincible){
		healthPoint -= damage;
		if (healthPoint >= 1){
			Sound::playSound(GameSound::BossScream2);
		}
		isInvincible = true;
		walkAnimIndex = 1;
	}
}
void Aquamentus::takeDamage(int damage){
	if (!isInvincible){
		if (healthPoint > damage)
			Sound::playSound(GameSound::BossScream2);
		healthPoint -= damage;
		isInvincible = true;
		walkAnimIndex = 1;
	}
}
void Aquamentus::movement(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	switch (dir){
	case Direction::Left:
		if (currentForwardDistance < maxForwardDistance){
			currentForwardDistance+=0.5;
			position.x-=0.5;
		}
		else dir = Direction::Right;
		break;
	case Direction::Right:
		position.x+=0.5;
		currentForwardDistance-=0.5;
		if (currentForwardDistance == 0)
			dir = Direction::Left;
		break;
	}
}
void Aquamentus::loadAnimation(){
	walkingAnimation.push_back(std::make_unique<Animation>("Aquamentus", height, width, position, 16));
	walkingAnimation.push_back(std::make_unique<Animation>("Aquamentus_Hit1", height, width, position, 16));
	walkingAnimation.push_back(std::make_unique<Animation>("Aquamentus_Hit2", height, width, position, 16));
}
