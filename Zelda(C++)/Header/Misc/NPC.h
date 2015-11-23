#ifndef NPC_H
#define NPC_H
#include "Misc\ShopObject.h"
#include "Type\NpcType.h"
class NPC :public ShopObject {
public:
	NPC(Point pos,NpcType type);
	void update(std::vector<std::shared_ptr<GameObject>>* Worldmap);
	void draw(sf::RenderWindow& mainWindow);
private:
	void loadImage();
	NpcType npcType;
};
#endif