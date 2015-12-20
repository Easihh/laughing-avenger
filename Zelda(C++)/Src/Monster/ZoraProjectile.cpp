#include "Monster\ZoraProjectile.h"
#include "Player\Player.h"
ZoraProjectile::ZoraProjectile(Point pos,Direction Projectiledir) {
	depth = 40;
	position = pos;
	strength = 2;
	dir = Projectiledir;
	width = Global::HalfTileWidth;
	height = 20;
	setupMask(&fullMask, width, height, sf::Color::Magenta);
	setupMonsterMask();
	isBeingDestroyed = false;
	projectileAnimation = std::make_unique<Animation>("ZoraProjectile", height, width, position, 8);
}
void ZoraProjectile::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(projectileAnimation->sprite);
	//mainWindow.draw(*fullMask);
}
void ZoraProjectile::projectileMovement(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	switch (dir){
	case Direction::Left:
		position.x -= projectileSpeed;
	break;
	case Direction::TopLeft:
		position.x -= projectileSpeed;
		position.y -= 1;
	break;
	case Direction::BottomLeft:
		position.x -= projectileSpeed;
		position.y += 1;
	break;
	}
	sprite.setPosition(position.x, position.y);
	if (isOutsideRoomBound(position)){
		destroyGameObject(worldMap);
		isBeingDestroyed = true;
	}
	fullMask->setPosition(position.x, position.y);
	mask->setPosition(position.x, position.y);
}
void ZoraProjectile::checkIfPlayerCanBlock(std::vector<std::shared_ptr<GameObject>>* worldMap){
	Player* temp = (Player*)findPlayer(worldMap).get();
	temp->takeDamage(worldMap, this);
	destroyGameObject(worldMap);
}
void ZoraProjectile::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	projectileMovement(worldMap);
	projectileAnimation->updateAnimationFrame(position);
	if (isCollidingWithPlayer(worldMap) && !isBeingDestroyed)
		checkIfPlayerCanBlock(worldMap);
}