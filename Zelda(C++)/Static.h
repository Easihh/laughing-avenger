#ifndef STATIC_H
#define STATIC_H

#include <string>
#include "SFML\Graphics.hpp"
namespace Global{
	extern const unsigned int FPS_RATE;
	extern const unsigned int SCREEN_WIDTH;
	extern const unsigned int SCREEN_HEIGHT;
	extern const int TileWidth;
	extern const int TileHeight;
	extern unsigned int CURRENT_FPS;
	extern const int minStep;
	extern const int HalfTileWidth;
	extern const int HalfTileHeight;
	extern const int minGridStep;
	extern const int roomWidth;
	extern const int roomHeight;
	extern const int roomCols;
	extern const int roomRows;
	extern const int inventoryHeight;
	extern sf::View gameView;
};
class Static{
public:
	static enum GameState{ NotStarted, Paused, Playing, Menu, Exiting }; 
	static enum Direction{ Right, Left, Up, Down };
	static  GameState gameState;
	const static std::string GAME_TITLE;
	static const int WorldRows=32;
	static const int WorldColumns=32;
};
#endif