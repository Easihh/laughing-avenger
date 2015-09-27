#include "Static.h"
namespace Global
{
	 const unsigned int FPS_RATE=60;
	 const unsigned int SCREEN_WIDTH = 608;
	 const unsigned int SCREEN_HEIGHT = 512;
	 const int TileWidth=32;
	 const int TileHeight=32;
	 unsigned int CURRENT_FPS=0;
	 const int minStep = 16;
}
Static::GameState Static::gameState=NotStarted;
const std::string Static::GAME_TITLE = "Zelda: Last Quest ";