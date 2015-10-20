#ifndef PLAYERINFO_H
#define PLAYERINFO_H
#include "Point.h"
#include "Static.h"
struct PlayerInfo
{
	int* bombAmount;
	int* diamondAmount;
	int* keysAmount;
	Point point;
	Static::Direction dir;
	PlayerInfo(Point pt, int* bomb, int* diamond, int* keys, Static::Direction direction);
};
#endif