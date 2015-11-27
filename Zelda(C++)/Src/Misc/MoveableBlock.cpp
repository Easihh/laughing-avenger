#include "Misc\MoveableBlock.h"
#include "Utility\Static.h"
#include "Player\Player.h"
MoveableBlock::MoveableBlock(Point pos){
	position = pos;
	originalPosition = pos;
	width = Global::TileHeight;
	height = Global::TileHeight;
	setupFullMask();
	texture.loadFromFile("Tileset/Dungeon/dungeonTile29.png");
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
	hasBeenPushed = false;
}
void MoveableBlock::resetPosition(){
	hasBeenPushed = false;
	position = originalPosition;
	sprite.setPosition(position.x, position.y);
	fullMask->setPosition(position.x, position.y);
}
void MoveableBlock::checkIfPlayerIsNear(std::vector<std::shared_ptr<GameObject>>* worldMap){
	Player* temp = (Player*)findPlayer(worldMap).get();
	Point player = temp->position;
	int distanceX = (int)position.x - player.x;
	int distanceY = (int)position.y - player.y;
	if (distanceY==0 && distanceX==32){//player is directly left
		Sound::playSound(GameSound::SecretRoom);
		position.x += width;
		hasBeenPushed = true;
	}
	else if (distanceY == 0 && distanceX == -Global::TileWidth && temp->dir == Direction::Right){//Player is right of block
		Sound::playSound(GameSound::SecretRoom);
		position.x -= width;
		hasBeenPushed = true;
	}
	else if (distanceY == -Global::TileHeight && distanceX == 0 && temp->dir == Direction::Up){//Player is under block
		Sound::playSound(GameSound::SecretRoom);
		position.y -= height;
		hasBeenPushed = true;
	}
	else if (distanceY == Global::TileHeight && distanceX == 0 && temp->dir == Direction::Down){//Player is top block
		Sound::playSound(GameSound::SecretRoom);
		position.y += height;
		hasBeenPushed = true;
	}
	sprite.setPosition(position.x, position.y);
	fullMask->setPosition(position.x, position.y);
}
void MoveableBlock::draw(sf::RenderWindow& mainWindow){
	mainWindow.draw(sprite);
}
void MoveableBlock::update(std::vector<std::shared_ptr<GameObject>>* worldMap){
	if (!hasBeenPushed)
		checkIfPlayerIsNear(worldMap);
}