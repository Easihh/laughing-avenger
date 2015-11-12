#include "Player\SwordDestroyEffect.h"
#include "Utility\Static.h"
SwordDestroyEffect::SwordDestroyEffect(Point pos,Direction dir) {
	position = pos;
	height = Global::HalfTileHeight;
	width = Global::HalfTileWidth;
	direction = dir;
	setAnimation();
	currentFrame = 0;
}
void SwordDestroyEffect::setAnimation() {
	switch(direction){
	case Direction::BottomLeft:
		swordEffectAnimation = std::make_unique<Animation>("sword_effect_bottomleft", height, width, position, 6);
		break;
	case Direction::BottomRight:
		swordEffectAnimation = std::make_unique<Animation>("sword_effect_bottomright", height, width, position, 6);
	break;
	case Direction::TopRight:
		swordEffectAnimation = std::make_unique<Animation>("sword_effect_topright", height, width, position, 6);
		break;
	case Direction::TopLeft:
		swordEffectAnimation = std::make_unique<Animation>("sword_effect_topleft", height, width, position, 6);
		break;
	}
}
void SwordDestroyEffect::movement() {
	switch(direction){
	case Direction::BottomLeft:
		position.x -= movingSpeed;
		position.y += movingSpeed;
		break;
	case Direction::BottomRight:
		position.x += movingSpeed;
		position.y += movingSpeed;
		break;
	case Direction::TopLeft:
		position.x -= movingSpeed;
		position.y -= movingSpeed;
		break;
	case Direction::TopRight:
		position.x += movingSpeed;
		position.y -= movingSpeed;
		break;
	}
}
void SwordDestroyEffect::draw(sf::RenderWindow& mainWindow) {
	mainWindow.draw(swordEffectAnimation.get()->sprite);
}
void SwordDestroyEffect::update(std::vector<std::shared_ptr<GameObject>>* worldMap) {
	movement();
	swordEffectAnimation.get()->updateAnimationFrame(position);
	currentFrame++;
	if(currentFrame > maxFrame)
		destroyGameObject(worldMap);
}