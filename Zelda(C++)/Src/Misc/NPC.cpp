#include "Misc\NPC.h"
#include "Utility\Static.h"
#include "Player\Player.h"
NPC::NPC(Point pos,NpcType type) {
	npcType = type;
	position = pos;
	position.x -= Global::HalfTileWidth;//to center align inbetween two grid tile.
	width = Global::TileWidth;
	height = Global::TileHeight;
	setupFullMask();
	isVisible = true;
	font.loadFromFile("zelda.ttf");
	txt.setFont(font);
	loadImage();
}
void NPC::loadImage() {
	switch(npcType){
	case NpcType::Merchant:
		texture.loadFromFile("Tileset/Merchant.png");
		break;
	case NpcType::MedecineWoman:
		texture.loadFromFile("Tileset/MedecineWoman.png");
		break;
	case NpcType::OldMan:
	texture.loadFromFile("Tileset/OldMan.png");
		break;
	}
	sprite.setTexture(texture);
	sprite.setPosition(position.x, position.y);
}
void NPC::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
}
void NPC::draw(sf::RenderWindow& mainWindow) {
	if(isVisible){
		mainWindow.draw(sprite);
	}
}