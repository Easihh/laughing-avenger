#include "Misc\ShopMarker.h"
#include "Utility\Static.h"
#include "Player\Player.h"
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
void ShopMarker::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	if(isCollidingWithPlayer(Worldmap)){
		Player* temp = ((Player*)player.get());
		temp->isInsideShop = true;
		temp->prevWorldX = temp->worldX;
		temp->prevWorldY = temp->worldY;
		temp->pointBeforeTeleport = std::make_unique<Point>(position.x, position.y + Global::TileHeight);
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