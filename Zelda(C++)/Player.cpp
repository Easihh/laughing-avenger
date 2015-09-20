#include "Player.h"
#include "Static.h"
 Player::Player(){
	 xPosition = 50;
	 yPosition = 50;
}
 Player::~Player(){
 }
 void Player::update(){
	 if (sf::Keyboard::isKeyPressed(sf::Keyboard::Left))
		 xPosition--;
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Right))
		 xPosition++;
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Up))
		 yPosition--;
	 else if (sf::Keyboard::isKeyPressed(sf::Keyboard::Down))
		 yPosition++;
 }
 void Player::draw(sf::RenderWindow& mainWindow){
	 sprite.setSize(sf::Vector2f(PLAYER_WIDTH, PLAYER_WIDTH));
	 sprite.setOutlineColor(sf::Color::Blue);
	 sprite.setFillColor(sf::Color::Black);
	 sprite.setOutlineThickness(1);
	 sprite.setPosition(xPosition, yPosition);
	 mainWindow.draw(sprite);
 }