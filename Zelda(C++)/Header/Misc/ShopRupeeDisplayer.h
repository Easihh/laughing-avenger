#ifndef SHOPRUPEEDISPLAYER_H
#define SHOPRUPEEDISPLAYER_H
#include "Misc\ShopObject.h"
#include "Misc\Animation.h"
class ShopRupeeDisplayer :public ShopObject {
public:
	ShopRupeeDisplayer(Point pos);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
	std::unique_ptr<Animation> anim;
	sf::Text txt;
	std::string price;
	sf::Font font;
};
#endif