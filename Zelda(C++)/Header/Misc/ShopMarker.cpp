#include "ShopMarker.h"
#include "Utility\Static.h"
ShopMarker::~ShopMarker() {}
ShopMarker::ShopMarker(Point pos) {
	position = pos;
	texture.loadFromFile("Tileset/BlackTile.png");
	sprite.setTexture(texture);
	width = Global::TileWidth;
	height = 4;
	sprite.setPosition(position.x, position.y);
	setupFullMask();
}
void ShopMarker::draw(sf::RenderWindow& window) {
	window.draw(sprite);
}
void ShopMarker::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {

}