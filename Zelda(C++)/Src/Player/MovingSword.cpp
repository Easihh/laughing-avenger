#include "Player\MovingSword.h"
#include "Monster\Monster.h"
#include "Player\SwordDestroyEffect.h"
MovingSword::MovingSword(Point pos,Direction attackDir,int power) {
	swordPower = power;
	position = pos;
	swordDir = attackDir;
	loadAnimation(attackDir);
	depth = 9999;
}
void MovingSword::loadAnimation(Direction attackDir) {
	int nextFrameInUpdates = 6;
	switch(attackDir){
	case Direction::Right:
		width = Global::TileWidth;
		height = Global::HalfTileHeight;
		position.x += Global::TileWidth;
		position.y +=	float(0.5)*Global::HalfTileHeight;
		swordAnimation = std::make_unique<Animation>("sword_thrown_right", height, width, position, nextFrameInUpdates);
		break;
	case Direction::Left:
		width = Global::TileWidth;
		height = Global::HalfTileHeight;
		position.x -= Global::TileWidth;
		position.y += float(0.5)*Global::HalfTileHeight;
		swordAnimation = std::make_unique<Animation>("sword_thrown_left", height, width, position, nextFrameInUpdates);
		break;
	case Direction::Down:
		width = Global::HalfTileWidth;
		height = Global::TileHeight;
		position.x += float(0.5)*Global::HalfTileWidth;
		position.y += Global::HalfTileHeight;
		swordAnimation = std::make_unique<Animation>("sword_thrown_down", height, width, position, nextFrameInUpdates);
		break;
	case Direction::Up:
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
	case Direction::Right:
		position.x += movingSpeed;
		break;
	case Direction::Left:
		position.x -= movingSpeed;
		break;
	case Direction::Down:
		position.y += movingSpeed;
		break;
	case Direction::Up:
		position.y -= movingSpeed;
		break;
	}
	fullMask->setPosition(position.x, position.y);
}
void MovingSword::createDestroyEffect() {
	Point pt(position.x - Global::HalfTileWidth, position.y);
	std::shared_ptr<GameObject> bottomleft = std::make_shared<SwordDestroyEffect>(pt,Direction::BottomLeft);
	Static::toAdd.push_back(bottomleft);
	pt.setPoint(position.x + Global::HalfTileWidth, position.y);
	std::shared_ptr<GameObject> bottomright = std::make_shared<SwordDestroyEffect>(pt,Direction::BottomRight);
	Static::toAdd.push_back(bottomright);
	pt.setPoint(position.x + Global::HalfTileWidth, position.y);
	std::shared_ptr<GameObject> topright = std::make_shared<SwordDestroyEffect>(pt,Direction::TopRight);
	Static::toAdd.push_back(topright);
	pt.setPoint(position.x - Global::HalfTileWidth, position.y);
	std::shared_ptr<GameObject> topleft = std::make_shared<SwordDestroyEffect>(pt,Direction::TopLeft);
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
	else if (isOutsideRoomBound(position)){
		destroyGameObject(worldMap);
		createDestroyEffect();
	}
}