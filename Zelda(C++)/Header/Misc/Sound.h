#ifndef SOUND_H
#define SOUND_H
#include <SFML/Audio.hpp>
#include "Type\SoundType.h"
#include <memory>
class Sound {
public:
	void static playSound(GameSound::SoundType file);
	void static stopSound(GameSound::SoundType file);
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
	static sf::Sound* Sound::getHit;
	static sf::Sound* Sound::dungeon;
	static sf::Sound* Sound::overworld;
	static sf::Sound* Sound::secretRoom;
	static sf::Sound* Sound::boomrang;
	static sf::Sound* Sound::triforce;
	static sf::Sound* Sound::getHeart; 
	static sf::Sound* Sound::shieldBlock;
	static sf::Sound* Sound::bossScream1;
	static sf::Sound* Sound::bossScream2;
};
#endif