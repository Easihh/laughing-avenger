#include "Misc\Marker\DungeonMarker.h"
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
			temp->currentLayer = Dungeon;
			temp->prevLayer = OverWorld;
			Sound::stopSound(GameSound::OverWorld);
			Sound::playSound(GameSound::Underworld);
			temp->prevWorldX = temp->worldX;
			temp->prevWorldY = temp->worldY;
			temp->pointBeforeTeleport = std::make_unique<Point>(position.x, position.y + Global::TileHeight);
			float teleportX = temp->worldY*Global::roomWidth + (0.5*Global::roomWidth);
			float teleportY = temp->worldX*Global::roomHeight + Global::roomHeight + Global::inventoryHeight - 2 * Global::TileHeight;
			temp->position = Point(teleportX, teleportY);
		}
		else if(temp->currentLayer == Dungeon){
			Sound::stopSound(GameSound::Underworld);
			Sound::playSound(GameSound::OverWorld);
			//previousWorld must be same as current room when moving to overworld layer
			int worldX = (int)(position.y / (Global::roomHeight+Global::inventoryHeight));
			int worldY = (int)(position.x / Global::roomWidth);
			temp->prevWorldX = worldX;
			temp->prevWorldY = worldY;
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