#include "Item\Triforce.h"
#include "Player\Player.h"
Triforce::Triforce(Point pos) {
	isObtained = false;
	currentFrame = 0;
	position = pos;
	texture.loadFromFile("Tileset/Triforce.png");
	width = Global::TileWidth;
	height = 4;
	setupMask(&fullMask, width, height, sf::Color::Magenta);
	setupMask(&mask, width, height, sf::Color::Cyan);
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
Point Triforce::getReturnPointForPlayerLeavingDungeon(DungeonLevel level){
	Point retVal;
	switch (level){
	case DungeonLevel::ONE:
		retVal.x = 1824;
		retVal.y = 1824;
		break;
	}
	return retVal;
}
void Triforce::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	sprite.setPosition(position.x, position.y);
	if (isCollidingWithPlayer(Worldmap) && !isObtained) {
		Player* tmp = ((Player*)findPlayer(Worldmap).get());
		position.y = tmp->position.y - Global::TileHeight;
		position.x = tmp->position.x;
		tmp->isObtainingItem = true;
		tmp->sprite.setPosition(tmp->position.x, tmp->position.y);
		Sound::stopSound(GameSound::Underworld);
		Sound::playSound(GameSound::NewInventoryItem);
		Sound::playSound(GameSound::Triforce);
		tmp->inventory->playerBar->healPlayerToFull();
		if (tmp->inventory->playerBar->currentDungeon == DungeonLevel::ONE)
			tmp->inventory->hasDungeon1Triforce = true;
		isObtained = true;
	}
	if (isObtained){
		currentFrame++;
		if (currentFrame > maxFrame){
			Player* temp = ((Player*)findPlayer(Worldmap).get());
			temp->isObtainingItem = false;
			Sound::playSound(GameSound::OverWorld);
			//previousWorld must be same as current room when moving to overworld layer otherwise the player
			//wont be moved to the correct vector and game will crash when looking for player in the new layer.
			temp->prevWorldX = temp->position.y / Global::roomHeight;
			temp->prevWorldY = temp->position.x / Global::roomWidth;
			temp->currentLayer = Layer::OverWorld;
			temp->prevLayer = Layer::Dungeon;
			temp->position = getReturnPointForPlayerLeavingDungeon(temp->inventory->playerBar->currentDungeon);
			temp->worldX = temp->position.y / Global::roomHeight;
			temp->worldY = temp->position.x / Global::roomWidth;
			float inventoryNewX = temp->worldY*Global::roomHeight;
			float inventoryNewY = temp->worldX*Global::roomWidth;
			temp->inventory->playerBar->currentDungeon = DungeonLevel::NONE;
			temp->inventory->setInventoryPosition(Point(inventoryNewX, inventoryNewY));
			temp->inventory->playerBar->setPlayerBar(Point(inventoryNewX, inventoryNewY));
			for (int i = 0; i < temp->walkingAnimation.size(); i++) {
				temp->walkingAnimation[i]->sprite.setPosition(temp->position.x, temp->position.y);
				temp->attackAnimation[i]->sprite.setPosition(temp->position.x, temp->position.y);
				temp->movePlayerToNewVector = true;
			}
			Global::gameView.setCenter(temp->worldY*Global::roomWidth + Global::SCREEN_WIDTH / 2,
				temp->worldX*Global::roomHeight + Global::SCREEN_HEIGHT / 2);
			destroyGameObject(Worldmap);
		}
	}
}
void Triforce::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(sprite);
}