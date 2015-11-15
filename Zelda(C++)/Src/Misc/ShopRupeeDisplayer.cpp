#include "Misc\ShopRupeeDisplayer.h"
#include "Utility\Static.h"
ShopRupeeDisplayer::ShopRupeeDisplayer(Point pos) {
	position = pos;
	position.y += Global::HalfTileHeight/2;
	origin = position;
	height = Global::TileHeight;
	width = Global::TileWidth;
	anim = std::make_unique<Animation>("rupee", height, width, position, 10);
	font.loadFromFile("zelda.ttf");
	txt.setFont(font);
	txt.setCharacterSize(14);
	txt.setString("X");
	txt.setPosition(position.x + Global::TileWidth, position.y + (Global::HalfTileHeight / 2));
	isVisible = true;
	isObtained = false;
}
void ShopRupeeDisplayer::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	anim->updateAnimationFrame(position);
}
void ShopRupeeDisplayer::draw(sf::RenderWindow& mainWindow) {
	if(isVisible){
		mainWindow.draw(anim.get()->sprite);
		mainWindow.draw(txt);
	}
}