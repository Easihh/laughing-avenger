#ifndef TELEPORTFROMARTIFACTROOM_H
#define TELEPORTFROMARTIFACTROOM_H
#include "Misc\GameObject.h"
class TeleportFromArtifactRoom :public GameObject {
public:
	TeleportFromArtifactRoom(Point position);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
};
#endif