#include "Item\ThrownBoomrang.h"
#include "Player\Player.h"
ThrownBoomrang::ThrownBoomrang(Point pos, Direction direction) {
	Sound::playSound(GameSound::Boomerang);
	position = pos;
	isReturning = false;
	boomrangDir = direction;
	width=Global::TileWidth;
	height = Global::TileHeight;
	setupFullMask();
	setupInitialPosition();
	boomrangAnimation = std::make_unique<Animation>("Boomerang", height, width, position, 8);
}
void ThrownBoomrang::setupInitialPosition() {
	switch(boomrangDir){
	case Direction::Right:
		position.x += Global::TileWidth;
		break;
	case Direction::Left:
		position.x -= Global::TileWidth;
		break;
	case Direction::Down:
		position.y += Global::TileHeight;
		break;
	case Direction::Up:
		position.y -= Global::TileHeight;
		break;
	}
	sprite.setPosition(position.x, position.y);
	fullMask->setPosition(position.x, position.y);
}
void ThrownBoomrang::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(boomrangAnimation->sprite);
	mainWindow.draw(*fullMask);
}
void ThrownBoomrang::setDiagonalSpeed() {
	Player* temp = (Player*)player.get();
	Point playerPos = temp->position;
	float ratio,factor;
	stepsYToPlayer = std::abs(position.y - playerPos.y);
	stepsXToPlayer = std::abs(position.x - playerPos.x);
	if(stepsXToPlayer > stepsYToPlayer && stepsYToPlayer!=0){
		ratio = stepsXToPlayer / stepsYToPlayer;
		diagonalXspeed = boomrangSpeed;
		factor = boomrangSpeed / ratio;
		diagonalYspeed = 1 * factor;
	}
	else if(stepsXToPlayer < stepsYToPlayer && stepsXToPlayer != 0)
	{
		ratio = stepsYToPlayer / stepsXToPlayer;
		factor = boomrangSpeed / ratio;
		diagonalYspeed = boomrangSpeed;
		diagonalXspeed = 1 * factor;
	}
}
void ThrownBoomrang::setCorrectDirection() {
	Player* temp = (Player*)player.get();
	Point playerPos = temp->position;
	if(playerPos.x<position.x){//boomerang is to the right of player
		if(playerPos.y > position.y)//player is below the boomerang
			boomrangDir = Direction::BottomLeft;
		else if(playerPos.y < position.y)//player is atop the boomerang
			boomrangDir = Direction::TopLeft;
		else boomrangDir = Direction::Left;
	}
	else if(playerPos.x>position.x){//boomerang is to the left of player
			if(playerPos.y > position.y)//player is below the boomerang
				boomrangDir = Direction::BottomRight;
			else if(playerPos.y < position.y)//player is atop the boomerang
				boomrangDir = Direction::TopRight;
			else boomrangDir = Direction::Right;
		}
	else if(playerPos.x == position.x){
		if(playerPos.y > position.y)//player is below the boomerang
			boomrangDir = Direction::Down;
		else if(playerPos.y < position.y)//player is atop the boomerang
			boomrangDir = Direction::Up;
	}
}
void ThrownBoomrang::boomrangMovement() {
	switch(boomrangDir){
	case Direction::Right:
		position.x += boomrangSpeed;
		break;
	case Direction::Left:
		position.x -= boomrangSpeed;
		break;
	case Direction::Down:
		position.y += boomrangSpeed;
		break;
	case Direction::Up:
		position.y -= boomrangSpeed;
		break;
	case Direction::BottomLeft:
		position.y += diagonalYspeed;
		position.x -= diagonalXspeed;
		break;
	case Direction::TopLeft:
		position.y -= diagonalYspeed;
		position.x -= diagonalXspeed;
		break;
	case Direction::BottomRight:
		position.y += diagonalYspeed;
		position.x += diagonalXspeed;
		break;
	case Direction::TopRight:
		position.y -= diagonalYspeed;
		position.x += diagonalXspeed;
		break;
	}
	sprite.setPosition(position.x, position.y);
	if(isOutsideRoomBound(position))
		isReturning = true;
	fullMask->setPosition(position.x, position.y);
}
void ThrownBoomrang::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	boomrangMovement();
	boomrangAnimation->updateAnimationFrame(position);
	if(isReturning){
		setCorrectDirection();
		setDiagonalSpeed();
	}
	if(isCollidingWithPlayer(worldMap)){
		Sound::stopSound(GameSound::Boomerang);
		destroyGameObject(worldMap);
		Player* temp = (Player*)player.get();
		temp->boomerangIsActive = false;
	}
}