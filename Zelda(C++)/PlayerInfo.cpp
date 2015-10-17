#include "PlayerInfo.h"

PlayerInfo::PlayerInfo(Point pt,int* bomb,int* diamond,int* keys,Static::Direction direction){
	bombAmount = bomb;
	diamondAmount = diamond;
	keysAmount = keys;
	point = pt;
	dir = direction;
}