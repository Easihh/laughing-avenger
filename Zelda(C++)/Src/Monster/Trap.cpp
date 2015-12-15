#include "Monster\Trap.h"
#include "Utility\Static.h"
#include "Player\Player.h"
Trap::Trap(Point pos, bool canBeCollidedWith) {
	position = pos;
	width = Global::TileWidth;
	height = Global::TileHeight;
	isCollideable = canBeCollidedWith;
	loadImage();
	healthPoint = 2;
	strength = 1;
	currentInvincibleFrame = 0;
	isInvincible = true;
	pushbackStep = 0;
	setupFullMask();
	setupMonsterMask();
	dir = Direction::None;
	walkAnimIndex = 0;
	currentDistance = 0;
	isActive = false;
	isReturning = false;
	moveSpeed = 4;
	//Trap use fullmask size when player touch it unlike other monster like octorok who use part of monster sprite.
	mask->setSize(fullMask->getSize());
}
void Trap::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(sprite);
}
void Trap::TrapMovement(){
	switch (dir){
	case Direction::Down:
			position.y += moveSpeed;
			currentDistance += moveSpeed;
		break;
	case Direction::Up:
		position.y -= moveSpeed;
		currentDistance += moveSpeed;
		break;
	case Direction::Left:
		position.x -= moveSpeed;
		currentDistance += moveSpeed;
		break;
	case Direction::Right:
		position.x += moveSpeed;
		currentDistance += moveSpeed;
		break;
	}
	sprite.setPosition(position.x, position.y);
	mask->setPosition(position.x, position.y);
	fullMask->setPosition(position.x, position.y);
	if (currentDistance >= maxDistance && !isReturning){
		currentDistance = 0;
		setReturnDirection();
		isReturning = true;
		moveSpeed = 2;
	}
	if (currentDistance >= maxDistance && isReturning){
		currentDistance = 0;
		isReturning = false;
		isActive = false;
		dir=Direction::None;
	}
}
void Trap::setReturnDirection(){
	switch (dir){
	case Direction::Right:
		dir = Direction::Left;
		break;
	case Direction::Left:
		dir = Direction::Right;
		break;
	case Direction::Down:
		dir = Direction::Up;
		break;
	case Direction::Up:
		dir = Direction::Down;
		break;
	}
}
void Trap::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	if (isActive)
		TrapMovement();
	Player* temp = (Player*)findPlayer(worldMap).get();
	if (isCollidingWithPlayer(worldMap))
		temp->takeDamage(worldMap, this);
	float xDiff = std::abs(position.x - temp->position.x);
	float yDiff = std::abs(position.y - temp->position.y);
	if (xDiff <= Global::TileWidth && temp->position.y < position.y && !isActive){
		dir = Direction::Up;
		isActive = true;
		moveSpeed = 4;
	}
	else if (xDiff <= Global::TileWidth && temp->position.y > position.y && !isActive){
		dir = Direction::Down;
		isActive = true;
		moveSpeed = 4;
	}
	else if (yDiff <= Global::TileWidth && temp->position.x < position.x && !isActive){
		dir = Direction::Left;
		isActive = true;
		moveSpeed = 4;
	}
	else if (yDiff <= Global::TileWidth && temp->position.x > position.x && !isActive){
		dir = Direction::Right;
		isActive = true;
		moveSpeed = 4;
	}
}
void Trap::loadImage() {
	texture.loadFromFile("Tileset/Trap.png");
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
