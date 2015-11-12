#ifndef PLAYERINFO_H
#define PLAYERINFO_H
#include "Point.h"
#include "Type\Direction.h"
struct PlayerInfo
{
	int* bombAmount;
	int* diamondAmount;
	int* keysAmount;
	Point point;
	Direction dir;
	PlayerInfo(Point pt, int* bomb, int* diamond, int* keys,Direction direction);
};
#endif