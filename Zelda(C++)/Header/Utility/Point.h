#ifndef POINT_H
#define POINT_H
struct Point{
	float x;
	float y;

	Point(float x, float y);
	Point();
	void addToPoint(Point add);
	void setPoint(float x, float y);
};
#endif