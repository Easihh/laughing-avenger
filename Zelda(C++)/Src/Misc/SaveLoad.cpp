#include "Misc\SaveLoad.h"
#include "Utility\Static.h"
SaveLoad::SaveLoad() {
	font.loadFromFile("zelda.ttf");
	nameTexture.loadFromFile("Tileset/name.png");
	lifeTexture.loadFromFile("Tileset/life.png");
	texture.loadFromFile("Tileset/Hero.png");
}
void SaveLoad::update(sf::Event& event) {
	if(event.type == sf::Event::KeyPressed && event.key.code == sf::Keyboard::Space){
		Static::gameState = GameState::Playing;
		Sound::playSound(GameSound::OverWorld);
	}
}
void SaveLoad::draw(sf::RenderWindow& mainWindow) {
	sf::Text txt("- S E L E C T -", font);
	txt.setColor(sf::Color::White);
	txt.setPosition(164, 48);
	txt.setCharacterSize(14);
	mainWindow.draw(txt);

	sf::RectangleShape rect;
	rect.setPosition(60, 100);
	rect.setOutlineColor(sf::Color(110, 93, 243));
	rect.setOutlineThickness(3);
	rect.setFillColor(sf::Color::Black);
	sf::Vector2f size(400, 400);
	rect.setSize(size);
	mainWindow.draw(rect);

	sprite.setTexture(nameTexture,true);
	sprite.setPosition(160, 88);
	mainWindow.draw(sprite);

	sprite.setTexture(lifeTexture, true);
	sprite.setPosition(300, 88);
	mainWindow.draw(sprite);

	sprite.setTexture(texture,true);
	sprite.setPosition(96, 128);
	mainWindow.draw(sprite);

	sprite.setPosition(96,224);
	mainWindow.draw(sprite);

	sprite.setPosition(96, 320);
	mainWindow.draw(sprite);

	txt.setString("REGISTER YOUR NAME");
	txt.setPosition(96, 384);
	mainWindow.draw(txt);

	txt.setString("ELIMINATION MODE");
	txt.setPosition(96, 428);
	mainWindow.draw(txt);

	mainWindow.display();
}