#include "Misc\Marker\ShopMarker.h"
#include "Utility\Static.h"
#include "Player\Player.h"
#include "Type\Layer.h"
ShopMarker::ShopMarker(Point pos) {
	position = pos;
	texture.loadFromFile("Tileset/BlackTile.png");
	sprite.setTexture(texture);
	width = Global::TileWidth;
	height = 3;
	sprite.setPosition(position.x, position.y);
	setupMask(&mask, width, height, sf::Color::Magenta);
}
void ShopMarker::draw(sf::RenderWindow& window) {
	window.draw(sprite);
}
void ShopMarker::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	if(isCollidingWithPlayer(Worldmap)){
		Player* temp = ((Player*)findPlayer(Worldmap).get());
		if (temp->hasMovedFromEntranceDoor){
			Sound::stopSound(GameSound::OverWorld);
			temp->currentLayer = Layer::InsideShop;
			temp->prevLayer = Layer::OverWorld;
			temp->prevWorldX = temp->worldX;
			temp->prevWorldY = temp->worldY;
			temp->pointBeforeTeleport = std::make_unique<Point>(position.x, position.y);
			float teleportX = temp->worldY*Global::roomWidth + (0.5*Global::roomWidth);
			float teleportY = temp->worldX*Global::roomHeight + Global::roomHeight + Global::inventoryHeight - 2 * Global::TileHeight;
			temp->position = Point(teleportX, teleportY);
			for (int i = 0; i < temp->walkingAnimation.size(); i++) {
				temp->walkingAnimation[i]->sprite.setPosition(temp->position.x, temp->position.y);
				temp->attackAnimation[i]->sprite.setPosition(temp->position.x, temp->position.y);
				temp->movePlayerToNewVector = true;
			}
		}
	}
}