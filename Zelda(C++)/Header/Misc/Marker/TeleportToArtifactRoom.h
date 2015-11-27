#ifndef TELEPORTTOARTIFACTROOM_H
#define TELEPORTTOARTIFACTROOM_H
#include "Misc\GameObject.h"
class TeleportToArtifactRoom :public GameObject {
public:
	TeleportToArtifactRoom(Point position);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
};
#endif