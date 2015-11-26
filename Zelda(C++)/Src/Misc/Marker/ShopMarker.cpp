#include "Misc\Marker\ShopMarker.h"
#include "Utility\Static.h"
#include "Player\Player.h"
#include "Type\Layer.h"
ShopMarker::ShopMarker(Point pos) {
	position = pos;
	texture.loadFromFile("Tileset/BlackTile.png");
	sprite.setTexture(texture);
	width = Global::TileWidth;
	height = 4;
	sprite.setPosition(position.x, position.y);
	setupFullMask();
}
void ShopMarker::draw(sf::RenderWindow& window) {
	window.draw(sprite);
}
Point ShopMarker::getPointBeforeTeleport(Direction dirBeforeEntering,Point playerPos) {
	float x;
	float y;
	switch(dirBeforeEntering){
	case Direction::Left:
		x = playerPos.x + 2 * Global::TileWidth;
		y = playerPos.y;
		break;
	case Direction::Right:
		x = playerPos.x - 2 * Global::TileWidth;
		y = playerPos.y;
		break;
	case Direction::Up:
		x = playerPos.x;
		y = playerPos.y + 2 * Global::TileHeight;
		break;
	case Direction::Down:
		x = playerPos.x;
		y = playerPos.y - 2 * Global::TileHeight;
		break;
	}
	return Point(x,y);
}
void ShopMarker::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	if(isCollidingWithPlayer(Worldmap)){
		Player* temp = ((Player*)findPlayer(Worldmap).get());
		Sound::stopSound(GameSound::OverWorld);
		temp->currentLayer=Layer::InsideShop;
		temp->prevLayer = Layer::OverWorld;
		temp->prevWorldX = temp->worldX;
		temp->prevWorldY = temp->worldY;
		temp->pointBeforeTeleport = std::make_unique<Point>(getPointBeforeTeleport(temp->dir,temp->position));
		float teleportX = temp->worldY*Global::roomWidth + (0.5*Global::roomWidth);
		float teleportY = temp->worldX*Global::roomHeight + Global::roomHeight + Global::inventoryHeight - 2 * Global::TileHeight;
		temp->position = Point(teleportX, teleportY);
		for(int i = 0; i < temp->walkingAnimation.size(); i++) {
			temp->walkingAnimation[i]->sprite.setPosition(temp->position.x, temp->position.y);
			temp->attackAnimation[i]->sprite.setPosition(temp->position.x, temp->position.y);
			temp->movePlayerToNewVector = true;
		}
	}
}