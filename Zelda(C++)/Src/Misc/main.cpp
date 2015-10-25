#include <Windows.h>
#include "Misc\Game.h"
//WINAPI WinMain(HINSTANCE inst, HINSTANCE prev, LPSTR cmd, int show)
int main()
{
	srand(time(0));
	Game game;
	game.Start();
	return 0;
}