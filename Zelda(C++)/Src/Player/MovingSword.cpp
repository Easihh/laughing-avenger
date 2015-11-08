#include "Player\MovingSword.h"

MovingSword::~MovingSword() {}
MovingSword::MovingSword(Point pos,Static::Direction attackDir) {
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
}
void MovingSword::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(swordAnimation.get()->sprite);
}
void MovingSword::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	swordAnimation.get()->updateAnimationFrame(position);
	swordMovement(worldMap);
}