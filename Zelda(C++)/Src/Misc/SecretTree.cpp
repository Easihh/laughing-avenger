#include "Misc\SecretTree.h"
#include "Utility\Static.h"
#include "Player\Player.h"
SecretTree::SecretTree(Point pos) {
	isCollideable = true;
	isActivated = false;
	position = pos;
	height = Global::TileHeight;
	width = Global::TileWidth;
	texture.loadFromFile("Tileset/Tree.png");
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
	setupMask(&fullMask, width, height, sf::Color::Magenta);
	setupMask(&mask, width, height, sf::Color::Cyan);
}
Point SecretTree::getPointBeforeTeleport(Direction dirBeforeEntering, Point playerPos) {
	float x;
	float y;
	switch(dirBeforeEntering){
	case Direction::Left:
		x = playerPos.x + 2 * Global::TileWidth;
		y = position.y;
		break;
	case Direction::Right:
		x = playerPos.x - 2 * Global::TileWidth;
		y = position.y;
		break;
	case Direction::Up:
		x = position.x;
		y = playerPos.y + 2 * Global::TileHeight;
		break;
	case Direction::Down:
		x = position.x;
		y = playerPos.y - 2 * Global::TileHeight;
		break;
	}
	return Point(x, y);
}
void SecretTree::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	if(isActivated){
		if(isCollidingWithPlayer(Worldmap)){
			Player* tmp = ((Player*)findPlayer(Worldmap).get());
			Sound::stopSound(GameSound::OverWorld);
			tmp->prevLayer = tmp->currentLayer;
			tmp->currentLayer = Layer::InsideShop;
			tmp->prevWorldX = tmp->worldX;
			tmp->prevWorldY = tmp->worldY;
			tmp->pointBeforeTeleport = std::make_unique<Point>(getPointBeforeTeleport(tmp->dir, tmp->position));
			float teleportX = tmp->worldY*Global::roomWidth + (0.5*Global::roomWidth);
			float teleportY = tmp->worldX*Global::roomHeight + Global::roomHeight + Global::inventoryHeight - 2 * Global::TileHeight;
			tmp->position = Point(teleportX, teleportY);
			for(int i = 0; i < tmp->walkingAnimation.size(); i++) {
				tmp->walkingAnimation[i]->sprite.setPosition(tmp->position.x, tmp->position.y);
				tmp->attackAnimation[i]->sprite.setPosition(tmp->position.x, tmp->position.y);
				tmp->movePlayerToNewVector = true;
			}
		}
	}
}
void SecretTree::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(sprite);
}