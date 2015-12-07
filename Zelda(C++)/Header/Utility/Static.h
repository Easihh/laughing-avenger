#ifndef STATIC_H
#define STATIC_H
#include "Misc\GameObject.h"
#include <string>
#include "SFML\Graphics.hpp"
#include "Type\GameState.h"
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
	extern const int playerMarkerWidth;
	extern const int playerMarkerHeight;
	extern const int overworldMapWidth;
	extern const int overworldMapHeight;
	extern const int WorldRoomWidth;
	extern const int WorldRoomHeight;
	extern sf::View gameView;
};
class Static{
public:
	static  GameState gameState;
	const static std::string GAME_TITLE;
	static const int inventoryRows=3;
	static const int inventoryCols=6;
	static std::vector<std::shared_ptr<GameObject>>  toAdd;
	static std::vector<std::shared_ptr<GameObject>>  toDelete;
	static std::vector<sf::Texture>  Static::gameResource;
};
#endif