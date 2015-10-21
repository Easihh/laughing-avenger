#include "Utility\Static.h"
namespace Global
{
	 const unsigned int FPS_RATE=60;
	 const unsigned int SCREEN_WIDTH = 512;
	 const unsigned int SCREEN_HEIGHT = 640;
	 const int TileWidth=32;
	 const int TileHeight=32;
	 const int HalfTileWidth = 16;
	 const int HalfTileHeight = 16;
	 unsigned int CURRENT_FPS=0;
	 const int minStep = 3;
	 const int minGridStep = 16;
	 const int roomWidth = 512;
	 const int roomHeight = 512;
	 const int roomCols = 16;
	 const int roomRows = 16;
	 const int inventoryHeight = 128;
	 const int playerMarkerWidth=8;
	 const int playerMarkerHeight = 8;
	 extern const int overworldMapWidth=128;
	 extern const int overworldMapHeight=64;
	 const int WorldRoomWidth=2;
	 const int WorldRoomHeight=2;
	 sf::View gameView;
}
Static::GameState Static::gameState=NotStarted;
std::vector<GameObject*> Static::toAdd;
std::vector<GameObject*> Static::toDelete;
const std::string Static::GAME_TITLE = "Zelda: Last Quest ";