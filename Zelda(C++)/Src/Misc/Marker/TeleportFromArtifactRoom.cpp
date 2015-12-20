#include "Misc\Marker\TeleportFromArtifactRoom.h"
#include "Utility\Static.h"
#include "Player\Player.h"
TeleportFromArtifactRoom::TeleportFromArtifactRoom(Point pos){
	position = pos;
	width = Global::TileWidth;
	height = Global::TileHeight;
	setupMask(&fullMask, width, height, sf::Color::Magenta);
	setupMask(&mask, width, height, sf::Color::Cyan);
	texture.loadFromFile("Tileset/Dungeon/dungeonTile30.png");
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void TeleportFromArtifactRoom::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
}
void TeleportFromArtifactRoom::update(std::vector<std::shared_ptr<GameObject>>* Worldmap){
	if (isCollidingWithPlayer(Worldmap)){
		Player* player = (Player*)findPlayer(Worldmap).get();
		Point pt = *player->pointBeforeTeleport.get();
		player->worldX = pt.y/Global::roomHeight;
		player->worldY = pt.x/Global::roomWidth;
		player->prevWorldX = position.y/Global::roomHeight;
		player->prevWorldY = position.x/Global::roomWidth;
		player->position = pt;
		float inventoryNewX = player->worldY*Global::roomHeight;
		float inventoryNewY = player->worldX*Global::roomWidth;
		player->inventory->setInventoryPosition(Point(inventoryNewX, inventoryNewY));
		player->inventory->playerBar->setPlayerBar(Point(inventoryNewX, inventoryNewY));
		player->movePlayerToNewVector = true;
		player->updateSprites();
		Global::gameView.setCenter(player->worldY*Global::roomWidth + Global::SCREEN_WIDTH / 2,
			player->worldX*Global::roomHeight + Global::SCREEN_HEIGHT / 2);
	}
}