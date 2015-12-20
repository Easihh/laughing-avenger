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
	healthPoint = 2;
	strength = 2;
	currentInvincibleFrame = 0;
	pushbackStep = 0;
	setupFullMask();
	setupMonsterMask();
	sf::Vector2f size(32, 24);
	fullMask->setSize(size);
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
	shoot(worldMap);
	for (int i = 0; i < 3; i++)
		walkingAnimation[i]->updateAnimationFrame(position);
	if (healthPoint <= 0){
		Point pt(position.x + (width / 4), position.y + (height / 4));
		std::shared_ptr<GameObject> add = std::make_shared<DeathEffect>(pt);
		Static::toAdd.push_back(add);
		destroyGameObject(worldMap);
		Sound::playSound(GameSound::SoundType::EnemyKill);
		dropItemOnDeath();
		std::cout << "Aquamentus Destroyed";
	}
	else
	{
		checkInvincibility();
	}
	updateMasks();
}
void Aquamentus::shoot(std::vector<std::shared_ptr<GameObject>>* worldMap){
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
