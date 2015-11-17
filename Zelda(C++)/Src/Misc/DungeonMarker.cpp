#include "Misc\DungeonMarker.h"
#include "Utility\Static.h"
#include "Player\Player.h"
#include "Type\Layer.h"
DungeonMarker::DungeonMarker(Point pos) {
	position = pos;
	position.x -= Global::HalfTileWidth;//to be in center of the  door entrance
	sprite.setTexture(texture);
	width = Global::TileWidth;
	height = 4;
	sprite.setPosition(position.x, position.y);
	setupFullMask();
}
void DungeonMarker::draw(sf::RenderWindow& window) {
	window.draw(sprite);
}
void DungeonMarker::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	if(isCollidingWithPlayer(Worldmap)){
		Player* temp = ((Player*)player.get());
		if(temp->currentLayer == OverWorld){
			temp->currentLayer = Layer::Dungeon;
			temp->prevLayer = Layer::OverWorld;
			temp->prevWorldX = temp->worldX;
			temp->prevWorldY = temp->worldY;
			temp->pointBeforeTeleport = std::make_unique<Point>(position.x, position.y + Global::TileHeight);
			float teleportX = temp->worldY*Global::roomWidth + (0.5*Global::roomWidth);
			float teleportY = temp->worldX*Global::roomHeight + Global::roomHeight + Global::inventoryHeight - 2 * Global::TileHeight;
			temp->position = Point(teleportX, teleportY);
		}
		else if(temp->currentLayer == Dungeon){
			temp->currentLayer = Layer::OverWorld;
			temp->prevLayer = Layer::Dungeon;
			temp->position = *temp->pointBeforeTeleport.get();
		}
		for(int i = 0; i < temp->walkingAnimation.size(); i++) {
			temp->walkingAnimation[i]->sprite.setPosition(temp->position.x, temp->position.y);
			temp->attackAnimation[i]->sprite.setPosition(temp->position.x, temp->position.y);
			temp->movePlayerToNewVector = true;
		}
	}
}