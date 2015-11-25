#include "Item\ThrownArrow.h"
#include "Monster\Monster.h"
#include "Player\Player.h"
ThrownArrow::ThrownArrow(Point pos,Direction direction) {
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
	case Direction::Down:
		width = 16;
		height = 32;
		position.y += height;
		position.x += float(0.5)*Global::HalfTileWidth;
		texture.loadFromFile("Tileset/Arrow_Down.png");
		break;
	case Direction::Up:
		width = 16;
		height = 32;
		position.y -= height;
		position.x += float(0.5)*Global::HalfTileWidth;
		texture.loadFromFile("Tileset/Arrow_Up.png");
		break;
	case Direction::Right:
		width = 32;
		height = 16;
		position.x += width;
		position.y += float(0.5)*Global::HalfTileHeight;
		texture.loadFromFile("Tileset/Arrow_Right.png");
		break;
	case Direction::Left:
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
	case Direction::Right:
		position.x += arrowSpeed;
		break;
	case Direction::Left:
		position.x -= arrowSpeed;
		break;
	case Direction::Up:
		position.y -= arrowSpeed;
		break;
	case Direction::Down:
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
		std::shared_ptr<GameObject> temp = findPlayer(worldMap);
		Player* player = (Player*)temp.get();
		player->arrowIsActive = false;
	}
	else if(isOutsideRoomBound(position)){
		destroyGameObject(worldMap);
		std::shared_ptr<GameObject> temp = findPlayer(worldMap);
		Player* player = (Player*)temp.get();
		player->arrowIsActive = false;
	}
}