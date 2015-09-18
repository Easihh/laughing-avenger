#ifndef GAME_MANAGER_H
#define GAME_MANAGER_H
#include <string>
#include "GameObject.h"
#include <map>
class GameManager{
public:
	GameManager();
	~GameManager();
	void updateAll();
	void add(std::string key, GameObject* obj);
	void remove(std::string key);
private:
	std::map<std::string, GameObject*> gameObjects;
	struct GameObjectDeallocator{
		void operator()(const std::pair<std::string, GameObject*>&p) const
		{
			delete p.second;
		}
	};
};

#endif