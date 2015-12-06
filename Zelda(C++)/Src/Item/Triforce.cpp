#include "Item\Triforce.h"
#include "Player\Player.h"
Triforce::Triforce(Point pos) {
	isObtained = false;
	currentFrame = 0;
	position = pos;
	texture.loadFromFile("Tileset/Triforce.png");
	width = Global::TileWidth;
	height = 4;
	setupFullMask();
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
		isObtained = true;
	}
	if (isObtained){
		currentFrame++;
		if (currentFrame > maxFrame){
			Player* temp = ((Player*)findPlayer(Worldmap).get());
			temp->isObtainingItem = false;
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
			temp->inventory->playerBar->healPlayerToFull();
			for (int i = 0; i < temp->walkingAnimation.size(); i++) {
				temp->walkingAnimation[i]->sprite.setPosition(temp->position.x, temp->position.y);
				temp->attackAnimation[i]->sprite.setPosition(temp->position.x, temp->position.y);
				temp->movePlayerToNewVector = true;
			}
			destroyGameObject(Worldmap);
		}
	}
}
void Triforce::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(sprite);
}