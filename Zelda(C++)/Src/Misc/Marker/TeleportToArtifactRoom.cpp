#include "Misc\Marker\TeleportToArtifactRoom.h"
#include "Player\Player.h"
TeleportToArtifactRoom::TeleportToArtifactRoom(Point pos){
	position = pos;
	width = Global::TileWidth;
	height = Global::TileHeight;
	setupMask(&fullMask, width, height, sf::Color::Magenta);
	setupMask(&mask, width, height, sf::Color::Cyan);
	texture.loadFromFile("Tileset/artifactRoomStair.png");
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void TeleportToArtifactRoom::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
}
void TeleportToArtifactRoom::update(std::vector<std::shared_ptr<GameObject>>* Worldmap){
	if (isCollidingWithPlayer(Worldmap)){
		Player* player = (Player*)findPlayer(Worldmap).get();
		player->prevLayer = Layer::Dungeon;
		player->pointBeforeTeleport = std::make_unique<Point>(position.x-3*Global::TileWidth, position.y+3*Global::TileHeight);
		player->prevWorldX = player->worldX;
		player->prevWorldY = player->worldY;
		player->worldX = 1;
		player->worldY = 4;
		player->position.x=2176;
		player->position.y = 752;
		float inventoryNewX = player->worldY*Global::roomHeight;
		float inventoryNewY = player->worldX*Global::roomWidth;
		player->inventory->setInventoryPosition(Point(inventoryNewX, inventoryNewY));
		player->inventory->playerBar->setPlayerBar(Point(inventoryNewX, inventoryNewY));
		player->movePlayerToNewVector = true;
		player->updateSprites();
		Global::gameView.setCenter(player->worldY*Global::roomWidth + Global::SCREEN_WIDTH/2, 
			player->worldX*Global::roomHeight + Global::SCREEN_HEIGHT / 2);
	}
}