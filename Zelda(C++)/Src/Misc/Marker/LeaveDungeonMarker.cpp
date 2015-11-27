#include "Misc\Marker\LeaveDungeonMarker.h"
#include "Utility\Static.h"
#include "Player\Player.h"
#include "Type\Layer.h"
LeaveDungeonMarker::LeaveDungeonMarker(Point pos) {
	position = pos;
	position.x -= Global::HalfTileWidth;//to be in center of the  door entrance
	sprite.setTexture(texture);
	width = Global::TileWidth;
	height = 4;
	sprite.setPosition(position.x, position.y);
	setupFullMask();
}
void LeaveDungeonMarker::draw(sf::RenderWindow& window) {
	window.draw(sprite);
}
Point LeaveDungeonMarker::getReturnPointForPlayerLeavingDungeon(DungeonLevel level){
	Point retVal;
	switch (level){
	case DungeonLevel::ONE:
		retVal.x = 1824;
		retVal.y = 1824;
		break;
	}
	return retVal;
}
void LeaveDungeonMarker::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	if(isCollidingWithPlayer(Worldmap)){
		Player* temp = ((Player*)findPlayer(Worldmap).get());
		Sound::stopSound(GameSound::Underworld);
		Sound::playSound(GameSound::OverWorld);
		//previousWorld must be same as current room when moving to overworld layer
		int worldX = (int)(position.y / (Global::roomHeight + Global::inventoryHeight));
		int worldY = (int)(position.x / Global::roomWidth);
		temp->prevWorldX = worldX;
		temp->prevWorldY = worldY;
		temp->currentLayer = Layer::OverWorld;
		temp->prevLayer = Layer::Dungeon;
		temp->position = getReturnPointForPlayerLeavingDungeon(temp->inventory->playerBar->currentDungeon);
		temp->inventory->playerBar->currentDungeon = DungeonLevel::NONE;
		for(int i = 0; i < temp->walkingAnimation.size(); i++) {
			temp->walkingAnimation[i]->sprite.setPosition(temp->position.x, temp->position.y);
			temp->attackAnimation[i]->sprite.setPosition(temp->position.x, temp->position.y);
			temp->movePlayerToNewVector = true;
		}
	}
}