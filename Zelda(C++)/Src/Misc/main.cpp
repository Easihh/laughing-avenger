#include <Windows.h>
#include "Misc\Game.h"
#include "Misc\GameResource.h"
//WINAPI WinMain(HINSTANCE inst, HINSTANCE prev, LPSTR cmd, int show)
int main()
{
	srand(time(0));
	GameResource resource;
	Game game;
	game.Start();
	return 0;
}