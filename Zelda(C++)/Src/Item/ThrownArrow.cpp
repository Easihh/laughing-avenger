#include "Item\ThrownArrow.h"
#include "Monster\Monster.h"
ThrownArrow::~ThrownArrow() {}
ThrownArrow::ThrownArrow(Point pos, Static::Direction direction) {
	position = pos;
	arrowDir = direction;
	width = Global::TileWidth;
	height = Global::TileHeight;
	setup();
	setupFullMask();
}
void ThrownArrow::setup() {
	switch(arrowDir)
	{
	case Static::Direction::Down:
		width = 16;
		height = 32;
		position.y += height;
		position.x += float(0.5)*Global::HalfTileWidth;
		texture.loadFromFile("Tileset/Arrow_Down.png");
		break;
	case Static::Direction::Up:
		width = 16;
		height = 32;
		position.y -= height;
		position.x += float(0.5)*Global::HalfTileWidth;
		texture.loadFromFile("Tileset/Arrow_Up.png");
		break;
	case Static::Direction::Right:
		width = 32;
		height = 16;
		position.x += width;
		position.y += float(0.5)*Global::HalfTileHeight;
		texture.loadFromFile("Tileset/Arrow_Right.png");
		break;
	case Static::Direction::Left:
		width = 32;
		height = 16;
		position.x -= width;
		position.y += float(0.5)*Global::HalfTileHeight;
		texture.loadFromFile("Tileset/Arrow_Left.png");
		break;
	}
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void ThrownArrow::arrowMovement() {
	switch(arrowDir){
	case Static::Right:
		position.x += arrowSpeed;
		break;
	case Static::Left:
		position.x -= arrowSpeed;
		break;
	case Static::Up:
		position.y -= arrowSpeed;
		break;
	case Static::Down:
		position.y += arrowSpeed;
		break;
	}
	sprite.setPosition(position.x, position.y);
	fullMask->setPosition(position.x, position.y);
}
void ThrownArrow::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(sprite);
	mainWindow.draw(*fullMask);
}
void ThrownArrow::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	arrowMovement();
	if(isCollidingWithMonster(worldMap)){
		if(!((Monster*)collidingMonster.get())->isInvincible)
			destroyGameObject(worldMap);
		((Monster*)collidingMonster.get())->takeDamage(arrowStrength, worldMap,arrowDir);
	}
	else if(isOutsideRoomBound(position))
		destroyGameObject(worldMap);
}