#include "Player\MovingSword.h"
#include "Monster\Monster.h"
#include "Player\SwordDestroyEffect.h"
MovingSword::~MovingSword() {}
MovingSword::MovingSword(Point pos,Static::Direction attackDir,int power) {
	swordPower = power;
	position = pos;
	swordDir = attackDir;
	loadAnimation(attackDir);
}
void MovingSword::loadAnimation(Static::Direction attackDir) {
	int nextFrameInUpdates = 6;
	switch(attackDir){
	case Static::Right:
		width = Global::TileWidth;
		height = Global::HalfTileHeight;
		position.x += Global::TileWidth;
		position.y +=	float(0.5)*Global::HalfTileHeight;
		swordAnimation = std::make_unique<Animation>("sword_thrown_right", height, width, position, nextFrameInUpdates);
		break;
	case Static::Left:
		width = Global::TileWidth;
		height = Global::HalfTileHeight;
		position.x -= Global::TileWidth;
		position.y += float(0.5)*Global::HalfTileHeight;
		swordAnimation = std::make_unique<Animation>("sword_thrown_left", height, width, position, nextFrameInUpdates);
		break;
	case Static::Down:
		width = Global::HalfTileWidth;
		height = Global::TileHeight;
		position.x += float(0.5)*Global::HalfTileWidth;
		position.y += Global::HalfTileHeight;
		swordAnimation = std::make_unique<Animation>("sword_thrown_down", height, width, position, nextFrameInUpdates);
		break;
	case Static::Up:
		width = Global::HalfTileWidth;
		height = Global::TileHeight;
		position.x += float(0.5)*Global::HalfTileWidth;
		position.y -= Global::HalfTileHeight;
		swordAnimation = std::make_unique<Animation>("sword_thrown_up", height, width, position, nextFrameInUpdates);
		break;
	}
	setupFullMask();
}
void MovingSword::swordMovement(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	switch(swordDir){
	case Static::Right:
		position.x += movingSpeed;
		if(isOutsideRoomBound(position))
			destroyGameObject(worldMap);
		break;
	case Static::Left:
		position.x -= movingSpeed;
		if(isOutsideRoomBound(position))
			destroyGameObject(worldMap);
		break;
	case Static::Down:
		position.y += movingSpeed;
		if(isOutsideRoomBound(position))
			destroyGameObject(worldMap);
		break;
	case Static::Up:
		position.y -= movingSpeed;
		if(isOutsideRoomBound(position))
			destroyGameObject(worldMap);
		break;
	}
	fullMask->setPosition(position.x, position.y);
}
void MovingSword::createDestroyEffect() {
	Point pt(position.x - Global::HalfTileWidth, position.y);
	std::shared_ptr<GameObject> bottomleft = std::make_shared<SwordDestroyEffect>(pt,Static::Direction::BottomLeft);
	Static::toAdd.push_back(bottomleft);
	pt.setPoint(position.x + Global::HalfTileWidth, position.y);
	std::shared_ptr<GameObject> bottomright = std::make_shared<SwordDestroyEffect>(pt, Static::Direction::BottomRight);
	Static::toAdd.push_back(bottomright);
	pt.setPoint(position.x + Global::HalfTileWidth, position.y);
	std::shared_ptr<GameObject> topright = std::make_shared<SwordDestroyEffect>(pt, Static::Direction::TopRight);
	Static::toAdd.push_back(topright);
	pt.setPoint(position.x - Global::HalfTileWidth, position.y);
	std::shared_ptr<GameObject> topleft = std::make_shared<SwordDestroyEffect>(pt, Static::Direction::TopLeft);
	Static::toAdd.push_back(topleft);

}
void MovingSword::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(swordAnimation.get()->sprite);
}
void MovingSword::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	swordAnimation.get()->updateAnimationFrame(position);
	swordMovement(worldMap);
	if(isCollidingWithMonster(worldMap)){
		if(!((Monster*)collidingMonster.get())->isInvincible){
			destroyGameObject(worldMap);
			createDestroyEffect();
		}
		((Monster*)collidingMonster.get())->takeDamage(swordPower,worldMap,swordDir);
	}
	else if(isOutsideRoomBound(position)){
		destroyGameObject(worldMap);
		createDestroyEffect();
	}
}