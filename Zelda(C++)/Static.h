#ifndef STATIC_H
#define STATIC_H

#include <string>
namespace Global{
	extern const unsigned int FPS_RATE;
	extern const unsigned int SCREEN_WIDTH;
	extern const unsigned int SCREEN_HEIGHT;
	extern const int TileWidth;
	extern const int TileHeight;
	extern unsigned int CURRENT_FPS;
	extern const int minStep;
};
class Static{
public:
	static enum GameState{ NotStarted, Paused, Playing, Menu, Exiting }; 
	static enum Direction{ Right, Left, Up, Down };
	static  GameState gameState;
	const static std::string GAME_TITLE;
	static const int WorldRows=16;
	static const int WorldColumns=16;
};
#endif