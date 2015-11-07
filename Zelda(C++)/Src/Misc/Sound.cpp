#include "Misc\Sound.h"
#include <iostream>
sf::SoundBuffer* Sound::buffer;
sf::Sound* Sound::bombDropSound;
sf::Sound* Sound::bombBlow;
sf::Sound* Sound::swordAttack;
sf::Sound* Sound::enemyTakeHit;
Sound::~Sound() {}
Sound::Sound() {
	buffer = new sf::SoundBuffer();
	if(!buffer->loadFromFile("Sound/bombDrop.wav"))
		std::cout << "Failed to load bombDrop.wav";
	bombDropSound = new sf::Sound();
	bombDropSound->setBuffer(*buffer);
	buffer = new sf::SoundBuffer();
	if(!buffer->loadFromFile("Sound/bombBlow.wav"))
		std::cout << "Failed to load bombBlow.wav";
	bombBlow = new sf::Sound();
	bombBlow->setBuffer(*buffer);
	buffer = new sf::SoundBuffer();
	if(!buffer->loadFromFile("Sound/sword.wav"))
		std::cout << "Failed to load sword.wav";
	swordAttack = new sf::Sound();
	swordAttack->setBuffer(*buffer);
	buffer = new sf::SoundBuffer();
	if(!buffer->loadFromFile("Sound/enemyHit.wav"))
		std::cout << "Failed to load enemyHit.wav";
	enemyTakeHit = new sf::Sound();
	enemyTakeHit->setBuffer(*buffer);
}
void Sound::playSound(SoundType sound) {
	if(sound == BombDrop)
		bombDropSound->play();
	else if(sound == BombExplose)
		bombBlow->play();
	else if(sound == SwordAttack)
		swordAttack->play();
	else if(sound == EnemyHit)
		enemyTakeHit->play();
}
