#ifndef TILEPARSER_H
#define TILEPARSER_H
#include "GameObject.h"
#include "Player\Player.h"
class TileParser {
public:
	TileParser();
	typedef std::vector<std::vector<std::vector<std::shared_ptr<GameObject>>>> tripleVector;
	void createTile(int lastWorldXIndex, int lastWorldYIndex, int tileType, tripleVector& objectVector, int vectorXIndex, int vectorYIndex);
private:
};
#endif