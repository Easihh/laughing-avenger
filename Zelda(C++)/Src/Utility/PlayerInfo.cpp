#include "Utility\PlayerInfo.h"
#include "Utility\Direction.h"
PlayerInfo::PlayerInfo(Point pt,int* bomb,int* diamond,int* keys,Direction direction){
	bombAmount = bomb;
	diamondAmount = diamond;
	keysAmount = keys;
	point = pt;
	dir = direction;
}