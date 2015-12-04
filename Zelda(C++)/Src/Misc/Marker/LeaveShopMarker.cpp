#include "Misc\Marker\LeaveShopMarker.h"
#include "Utility\Static.h"
#include "Player\Player.h"
#include "Type\Layer.h"
LeaveShopMarker::LeaveShopMarker(Point pos) {
	position = pos;
	position.x -= Global::HalfTileWidth;//to be in center of the  door entrance
	texture.loadFromFile("Tileset/BlackTile.png");
	sprite.setTexture(texture);
	width = Global::TileWidth;
	height = 4;
	sprite.setPosition(position.x, position.y);
	setupFullMask();
}
void LeaveShopMarker::draw(sf::RenderWindow& window) {
	window.draw(sprite);
}
void LeaveShopMarker::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	if(isCollidingWithPlayer(Worldmap)){
		Player* temp = ((Player*)findPlayer(Worldmap).get());
		Sound::playSound(GameSound::OverWorld);
		temp->currentLayer = Layer::OverWorld;
		temp->prevLayer = Layer::InsideShop;
		temp->position = *temp->pointBeforeTeleport.get();
		for(int i = 0; i < temp->walkingAnimation.size(); i++) {
			temp->walkingAnimation[i]->sprite.setPosition(temp->position.x, temp->position.y);
			temp->attackAnimation[i]->sprite.setPosition(temp->position.x, temp->position.y);
			temp->movePlayerToNewVector = true;
			temp->hasMovedFromEntranceDoor = false;
		}
	}
}