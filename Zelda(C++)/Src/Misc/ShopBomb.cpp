#include "Misc\ShopBomb.h"
#include "Utility\Static.h"
#include "Player\Player.h"
 ShopBomb::ShopBomb(Point pos){
	 position = pos;
	 origin = pos;
	 currentFrame = 0;
	 width = Global::TileWidth;
	 height = 4;
	 setupFullMask();
	 isVisible = true;
	 isObtained = false;
	 texture.loadFromFile("Tileset/Bomb.png");
	 sprite.setTexture(texture);
	 sprite.setPosition(position.x, position.y);
}
 void ShopBomb::update(std::vector<std::shared_ptr<GameObject>>* Worldmap) {
	 sprite.setPosition(position.x, position.y);
	 if(isCollidingWithPlayer(Worldmap) && !isObtained && isVisible) {
		 Player* tmp = ((Player*)player.get());
		 position.y = tmp->position.y - Global::TileHeight;
		 position.x = tmp->position.x;
		 tmp->isObtainingItem = true;
		 tmp->sprite.setPosition(tmp->position.x, tmp->position.y);
		 Sound::playSound(SoundType::NewItem);
		 Sound::playSound(SoundType::NewInventoryItem);
		 tmp->inventory->playerBar->increaseBombAmount(bombPerPurchase);
		 isObtained = true;
	 }
	 if(isObtained){
		 currentFrame++;
		 if(currentFrame > maxFrame){
			 currentFrame = 0;
			 Player* tmp = ((Player*)player.get());
			 tmp->isObtainingItem = false;
			 resetShopItem();
		 }
	 }
 }
 void ShopBomb::draw(sf::RenderWindow& mainWindow) {
	 if(isVisible)
		mainWindow.draw(sprite);
 }