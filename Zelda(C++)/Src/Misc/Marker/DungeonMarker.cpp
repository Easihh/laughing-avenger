#include "Misc\Marker\DungeonMarker.h"
#include "Utility\Static.h"
#include "Player\Player.h"
#include "Type\Layer.h"
DungeonMarker::DungeonMarker(Point pos) {
	position = pos;
	texture.loadFromFile("Tileset/BlackTile.png");
	sprite.setTexture(texture);
	width = Global::TileWidth;
	height = 4;
	sprite.setPosition(position.x, position.y);
	setupMask(&fullMask, width, height, sf::Color::Magenta);
	setupMask(&mask, width, height, sf::Color::Cyan);
}
void DungeonMarker::draw(sf::RenderWindow& window) {
	window.draw(sprite);
}
void DungeonMarker::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	if(isCollidingWithPlayer(Worldmap)){
		Player* temp = ((Player*)findPlayer(Worldmap).get());
		temp->currentLayer = Dungeon;
		temp->prevLayer = OverWorld;
		Sound::stopSound(GameSound::OverWorld);
		Sound::playSound(GameSound::Underworld);
		temp->prevWorldX = temp->worldX;
		temp->prevWorldY = temp->worldY;
		temp->inventory->playerBar->currentDungeon = DungeonLevel::ONE;
		temp->inventory->playerBar->resetDungeonPlayerMarker();
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