#ifndef SAVELOAD_H
#define SAVELOAD_H
#include "SFML\Graphics.hpp"
class SaveLoad {
public:
	SaveLoad();
	void update(sf::Event& event);
	void draw(sf::RenderWindow& mainWindow);
private:
	sf::Font font;
	sf::Texture texture,nameTexture,lifeTexture;
	sf::Sprite sprite;
};
#endif