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
sf::Sound* Sound::candleFire;
sf::Sound* Sound::getHit;
sf::Sound* Sound::dungeon;
sf::Sound* Sound::overworld;
sf::Sound* Sound::secretRoom;
sf::Sound* Sound::boomrang;
sf::Sound* Sound::triforce;
sf::Sound* Sound::getHeart;
sf::Sound* Sound::shieldBlock;
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
	buffer = new sf::SoundBuffer();
	if(!buffer->loadFromFile("Sound/candle.wav"))
		std::cout << "Failed to load candle.wav";
	candleFire = new sf::Sound();
	candleFire->setBuffer(*buffer);
	buffer = new sf::SoundBuffer();
	if(!buffer->loadFromFile("Sound/linkHurt.wav"))
		std::cout << "Failed to load linkHurt.wav";
	getHit = new sf::Sound();
	getHit->setBuffer(*buffer);
	buffer = new sf::SoundBuffer();
	if(!buffer->loadFromFile("Sound/Underworld.ogg"))
		std::cout << "Failed to load Underworld.ogg";
	dungeon = new sf::Sound();
	dungeon->setBuffer(*buffer);
	dungeon->setLoop(true);
	buffer = new sf::SoundBuffer();
	if(!buffer->loadFromFile("Sound/Overworld.ogg"))
		std::cout << "Failed to load Overworld.ogg";
	overworld = new sf::Sound();
	overworld->setBuffer(*buffer);
	overworld->setLoop(true);
	buffer = new sf::SoundBuffer();
	if(!buffer->loadFromFile("Sound/secret.wav"))
		std::cout << "Failed to load secret.wav";
	secretRoom = new sf::Sound();
	secretRoom->setBuffer(*buffer);
	buffer = new sf::SoundBuffer();
	if(!buffer->loadFromFile("Sound/boomerang.wav"))
		std::cout << "Failed to load boomerang.wav";
	boomrang = new sf::Sound();
	boomrang->setBuffer(*buffer);
	boomrang->setLoop(true);
	buffer = new sf::SoundBuffer();
	if (!buffer->loadFromFile("Sound/Triforce.ogg"))
		std::cout << "Failed to load Triforce.ogg";
	triforce = new sf::Sound();
	triforce->setBuffer(*buffer);
	buffer = new sf::SoundBuffer();
	if (!buffer->loadFromFile("Sound/getHeart.wav"))
		std::cout << "Failed to load getHeart.wav";
	getHeart = new sf::Sound();
	getHeart->setBuffer(*buffer);
	buffer = new sf::SoundBuffer();
	if (!buffer->loadFromFile("Sound/shieldBlock.wav"))
		std::cout << "Failed to load shieldBlock.wav";
	shieldBlock = new sf::Sound();
	shieldBlock->setBuffer(*buffer);
}
void Sound::stopSound(GameSound::SoundType sound) {
	if(sound == GameSound::Underworld)
		dungeon->stop();
	else if(sound == GameSound::OverWorld)
		overworld->stop();
	else if(sound == GameSound::Boomerang)
		boomrang->stop();
}
void Sound::playSound(GameSound::SoundType sound) {
	if(sound == GameSound::BombDrop)
		bombDropSound->play();
	else if(sound == GameSound::BombExplose)
		bombBlow->play();
	else if(sound == GameSound::SwordAttack)
		swordAttack->play();
	else if(sound == GameSound::EnemyHit)
		enemyTakeHit->play();
	else if(sound == GameSound::EnemyKill)
		enemyKilled->play();
	else if(sound == GameSound::Selector)
		selectorSound->play();
	else if(sound == GameSound::SwordCombineAttack)
		swordCombine->play();
	else if(sound == GameSound::ArrowThrown)
		arrow->play();
	else if(sound == GameSound::NewItem)
		itemNew->play();
	else if(sound == GameSound::NewInventoryItem)
		itemInventoryNew->play();
	else if(sound == GameSound::CandleFire)
		candleFire->play();
	else if(sound == GameSound::TakeDamage)
		getHit->play();
	else if(sound == GameSound::Underworld)
		dungeon->play();
	else if(sound == GameSound::OverWorld)
		overworld->play();
	else if(sound == GameSound::SecretRoom)
		secretRoom->play();
	else if(sound == GameSound::Boomerang)
		boomrang->play();
	else if (sound == GameSound::Triforce)
		triforce->play();
	else if (sound == GameSound::GetHeart)
		getHeart->play();
	else if (sound == GameSound::ShieldBlock)
		shieldBlock->play();
}
