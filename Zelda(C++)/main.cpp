#include <Windows.h>
#include "Game.h"
int WINAPI WinMain(HINSTANCE inst, HINSTANCE prev, LPSTR cmd, int show)
{
	Game game;
	game.Start();
	return 0;
}