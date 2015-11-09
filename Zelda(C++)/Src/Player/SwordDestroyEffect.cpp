#include "Player\SwordDestroyEffect.h"
SwordDestroyEffect::~SwordDestroyEffect() {}
SwordDestroyEffect::SwordDestroyEffect(Point pos, Static::Direction dir) {
	position = pos;
	height = Global::HalfTileHeight;
	width = Global::HalfTileWidth;
	direction = dir;
	setAnimation();
	currentFrame = 0;
}
void SwordDestroyEffect::setAnimation() {
	switch(direction){
	case Static::BottomLeft:
		swordEffectAnimation = std::make_unique<Animation>("sword_effect_bottomleft", height, width, position, 6);
		break;
	case Static::BottomRight:
		swordEffectAnimation = std::make_unique<Animation>("sword_effect_bottomright", height, width, position, 6);
	break;
	case Static::TopRight:
		swordEffectAnimation = std::make_unique<Animation>("sword_effect_topright", height, width, position, 6);
		break;
	case Static::TopLeft:
		swordEffectAnimation = std::make_unique<Animation>("sword_effect_topleft", height, width, position, 6);
		break;
	}
}
void SwordDestroyEffect::movement() {
	switch(direction){
	case Static::BottomLeft:
		position.x -= movingSpeed;
		position.y += movingSpeed;
		break;
	case Static::BottomRight:
		position.x += movingSpeed;
		position.y += movingSpeed;
		break;
	case Static::TopLeft:
		position.x -= movingSpeed;
		position.y -= movingSpeed;
		break;
	case Static::TopRight:
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