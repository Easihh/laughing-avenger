#ifndef STATIC_H
#define STATIC_H
class Static{
public:
	const static unsigned int FPS_RATE;
	const static unsigned int SCREEN_WIDTH;
	const static unsigned int SCREEN_HEIGHT;
	static enum GameState{ NotStarted, Paused, Playing, Menu, Exiting };
	static GameState gameState;
};
#endif