#ifndef STATIC_H
#define STATIC_H

#include <string>
#include "GameObject.h"
class Static{
public:
	const static unsigned int FPS_RATE;
	const static unsigned int SCREEN_WIDTH;
	const static unsigned int SCREEN_HEIGHT;
	static unsigned int CURRENT_FPS;
	static enum GameState{ NotStarted, Paused, Playing, Menu, Exiting }; 
	static GameState gameState;
	const static std::string GAME_TITLE;
	static bool intersect(GameObject* RectA, GameObject* RectB, int offsetX,int offsetY);
};
#endif