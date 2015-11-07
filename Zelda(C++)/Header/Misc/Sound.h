#ifndef SOUND_H
#define SOUND_H
#include <SFML/Audio.hpp>
#include "Misc\SoundType.h"
#include <memory>
class Sound {
public:
	void static playSound(SoundType file);
	Sound();
	~Sound();
private:
	static sf::SoundBuffer* buffer;
	static sf::Sound* bombDropSound;
	static sf::Sound* bombBlow;
	static sf::Sound* swordAttack;
	static sf::Sound* enemyTakeHit;
};
#endif