#include "ShopMarker.h"
#include "Utility\Static.h"
#include "Player\Player.h"
ShopMarker::~ShopMarker() {}
ShopMarker::ShopMarker(Point pos) {
	position = pos;
	texture.loadFromFile("Tileset/BlackTile.png");
	sprite.setTexture(texture);
	width = Global::TileWidth;
	height = 4;
	sprite.setPosition(position.x, position.y);
	setupFullMask();
}
bool ShopMarker::isCollidingWithPlayer(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	bool isColliding = false;
	Point offset(0, 0);
	if(player == NULL){
		for(auto& obj : *worldMap)
			if(dynamic_cast<Player*>(obj.get()))
				player = obj;
	}
	if(intersect(player->fullMask, fullMask, offset))
		isColliding = true;
	return isColliding;
}
void ShopMarker::draw(sf::RenderWindow& window) {
	window.draw(sprite);
}
void ShopMarker::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	if(isCollidingWithPlayer(Worldmap)){
		((Player*)player.get())->isInsideShop = true;
		((Player*)player.get())->prevWorldX = ((Player*)player.get())->worldX;
		((Player*)player.get())->prevWorldY = ((Player*)player.get())->worldY;
		((Player*)player.get())->pointBeforeTeleport = std::make_unique<Point>(position.x,position.y+Global::TileHeight);
		float teleportX = ((Player*)player.get())->worldY*Global::roomWidth + (0.5*Global::roomWidth);
		float teleportY = ((Player*)player.get())->worldX*Global::roomHeight + Global::roomHeight + Global::inventoryHeight - 2 * Global::TileHeight;
		((Player*)player.get())->position = Point(teleportX, teleportY);
		for(int i = 0; i < ((Player*)player.get())->walkingAnimation.size(); i++) {
			((Player*)player.get())->walkingAnimation[i]->sprite.setPosition(((Player*)player.get())->position.x, ((Player*)player.get())->position.y);
			((Player*)player.get())->attackAnimation[i]->sprite.setPosition(((Player*)player.get())->position.x, ((Player*)player.get())->position.y);
		((Player*)player.get())->movePlayerToNewVector = true;
		}
	}
}