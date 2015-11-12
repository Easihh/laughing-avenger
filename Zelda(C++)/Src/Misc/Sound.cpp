#include "Misc\Sound.h"
#include <iostream>
sf::SoundBuffer* Sound::buffer;
sf::Sound* Sound::bombDropSound;
sf::Sound* Sound::bombBlow;
sf::Sound* Sound::swordAttack;
sf::Sound* Sound::enemyTakeHit;
sf::Sound* Sound::enemyKilled;
sf::Sound* Sound::selectorSound;
sf::Sound* Sound::swordCombine;
sf::Sound* Sound::arrow;
sf::Sound* Sound::itemNew;
sf::Sound* Sound::itemInventoryNew;
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
	buffer = new sf::SoundBuffer();
	if(!buffer->loadFromFile("Sound/enemyKill.wav"))
		std::cout << "Failed to load enemyKill.wav";
	enemyKilled = new sf::Sound();
	enemyKilled->setBuffer(*buffer);
	buffer = new sf::SoundBuffer();
	if(!buffer->loadFromFile("Sound/selector.wav"))
		std::cout << "Failed to load selector.wav";
	selectorSound = new sf::Sound();
	selectorSound->setBuffer(*buffer);
	buffer = new sf::SoundBuffer();
	if(!buffer->loadFromFile("Sound/swordCombine.wav"))
		std::cout << "Failed to load swordCombine.wav";
	swordCombine = new sf::Sound();
	swordCombine->setBuffer(*buffer);
	buffer = new sf::SoundBuffer();
	if(!buffer->loadFromFile("Sound/arrow.wav"))
		std::cout << "Failed to load arrow.wav";
	arrow = new sf::Sound();
	arrow->setBuffer(*buffer);
	buffer = new sf::SoundBuffer();
	if(!buffer->loadFromFile("Sound/newItem.wav"))
		std::cout << "Failed to load newItem.wav";
	itemNew = new sf::Sound();
	itemNew->setBuffer(*buffer);
	buffer = new sf::SoundBuffer();
	if(!buffer->loadFromFile("Sound/newInventItem.wav"))
		std::cout << "Failed to load newInventItem.wav";
	itemInventoryNew = new sf::Sound();
	itemInventoryNew->setBuffer(*buffer);
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
	else if(sound == EnemyKill)
		enemyKilled->play();
	else if(sound == Selector)
		selectorSound->play();
	else if(sound == SwordCombineAttack)
		swordCombine->play();
	else if(sound == ArrowThrown)
		arrow->play();
	else if(sound == NewItem)
		itemNew->play();
	else if(sound == NewInventoryItem)
		itemInventoryNew->play();
}
