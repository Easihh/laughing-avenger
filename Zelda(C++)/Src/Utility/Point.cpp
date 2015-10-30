#include "Utility\Point.h"

Point::Point(float xPos, float yPos){
	x = xPos;
	y = yPos;
}
Point::Point(){}
void Point::setPoint(float px, float py){
	x = px;
	y = py;
}
Point& Point::operator+=(const Point& p){
	x+= + p.x;
	y+= + p.y;
	return *this;
}