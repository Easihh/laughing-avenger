#include "Static.h"
const unsigned int Static::FPS_RATE = 60;
const unsigned int Static::SCREEN_WIDTH = 608;
const unsigned int Static::SCREEN_HEIGHT = 512;
Static::GameState Static::gameState=NotStarted;
const std::string Static::GAME_TITLE = "Zelda: Last Quest ";

bool Static::intersect(GameObject* rectA,GameObject* rectB, int offsetX,int offsetY){
	return(
		rectA->xPosition + offsetX < rectB->xPosition + rectB->width  &&
		rectA->xPosition + rectA->width +offsetX > rectB->xPosition &&
		rectA->yPosition + offsetY < rectB->yPosition + rectB->height &&
		rectA->yPosition + rectA->height +offsetY > rectB->yPosition);
}