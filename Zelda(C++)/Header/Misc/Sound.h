#ifndef SOUND_H
#define SOUND_H
#include <SFML/Audio.hpp>
#include "Type\SoundType.h"
#include <memory>
class Sound {
public:
	void static playSound(SoundType file);
	Sound();
private:
	static sf::SoundBuffer* buffer;
	static sf::Sound* bombDropSound;
	static sf::Sound* bombBlow;
	static sf::Sound* swordAttack;
	static sf::Sound* enemyTakeHit;
	static sf::Sound* enemyKilled;
	static sf::Sound* selectorSound;
	static sf::Sound* swordCombine;
	static sf::Sound* arrow;
	static sf::Sound* itemNew;
	static sf::Sound* itemInventoryNew;
	static sf::Sound* candleFire;
};
#endif