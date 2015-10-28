#include "Utility\Point.h"

Point::Point(float xPos, float yPos){
	x = xPos;
	y = yPos;
}
Point::Point(){}
void Point::addToPoint(Point add){
	x += add.x;
	y += add.y;
}
void Point::setPoint(float px, float py){
	x = px;
	y = py;
}