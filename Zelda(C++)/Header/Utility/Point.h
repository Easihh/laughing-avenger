#ifndef POINT_H
#define POINT_H
struct Point{
	float x;
	float y;

	Point(float x, float y);
	Point();
	void setPoint(float x, float y);
	Point& operator+=(const Point& p);
};
#endif