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
sf::Sound* Sound::bossScream1;
sf::Sound* Sound::bossScream2;
sf::Sound* Sound::itemAppear;
sf::Sound* Sound::unlock;
sf::Sound* Sound::lowHealth;
sf::Sound* Sound::gameOver;
bool Sound::allSoundMuted;
Sound::Sound() {
	allSoundMuted = false;
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
	buffer = new sf::SoundBuffer();
	if (!buffer->loadFromFile("Sound/bossScream1.wav"))
		std::cout << "Failed to load bossScream1.wav";
	bossScream1 = new sf::Sound();
	bossScream1->setBuffer(*buffer);
	bossScream1->setLoop(true);
	buffer = new sf::SoundBuffer();
	if (!buffer->loadFromFile("Sound/bossScream2.wav"))
		std::cout << "Failed to load bossScream2.wav";
	bossScream2 = new sf::Sound();
	bossScream2->setBuffer(*buffer);
	buffer = new sf::SoundBuffer();
	if (!buffer->loadFromFile("Sound/itemAppear.wav"))
		std::cout << "Failed to load itemAppear.wav";
	itemAppear = new sf::Sound();
	itemAppear->setBuffer(*buffer);
	buffer = new sf::SoundBuffer();
	if (!buffer->loadFromFile("Sound/unlock.wav"))
		std::cout << "Failed to load unlock.wav";
	unlock = new sf::Sound();
	unlock->setBuffer(*buffer);
	buffer = new sf::SoundBuffer();
	if (!buffer->loadFromFile("Sound/lowHealth.wav"))
		std::cout << "Failed to load lowHealth.wav";
	lowHealth = new sf::Sound();
	lowHealth->setBuffer(*buffer);
	lowHealth->setLoop(true);
	buffer = new sf::SoundBuffer();
	if (!buffer->loadFromFile("Sound/gameOver.ogg"))
		std::cout << "Failed to load gameOver.ogg";
	gameOver = new sf::Sound();
	gameOver->setBuffer(*buffer);
	gameOver->setLoop(true);
}
void Sound::stopSound(GameSound::SoundType sound) {
	if(sound == GameSound::Underworld)
		dungeon->stop();
	else if(sound == GameSound::OverWorld)
		overworld->stop();
	else if(sound == GameSound::Boomerang)
		boomrang->stop();
	else if (sound == GameSound::BossScream1)
		bossScream1->stop();
	else if (sound == GameSound::LowHealth)
		lowHealth->stop();
}
void Sound::stopAllSounds(){
	allSoundMuted = true;
	bombDropSound->stop();
	bombBlow->stop();
	swordAttack->stop();
	enemyTakeHit->stop();
	enemyKilled->stop();
	selectorSound->stop();
	swordCombine->stop();
	arrow->stop();
	itemNew->stop();
	itemInventoryNew->stop();
	candleFire->stop();
	getHit->stop();
	dungeon->stop();
	overworld->stop();
	secretRoom->stop();
	boomrang->stop();
	triforce->stop();
	getHeart->stop();
	shieldBlock->stop();
	bossScream1->stop();
	bossScream2->stop();
	itemAppear->stop();
	unlock->stop();
	lowHealth->stop();
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
	else if (sound == GameSound::BossScream1){
		if (bossScream1->getStatus()==sf::SoundSource::Stopped)
			bossScream1->play();
	}
	else if (sound == GameSound::BossScream2)
		bossScream2->play();
	else if (sound == GameSound::ItemAppear)
		itemAppear->play();
	else if (sound == GameSound::DoorUnlock)
		unlock->play();
	else if (sound == GameSound::LowHealth){
		if (lowHealth->getStatus() == sf::SoundSource::Stopped && !allSoundMuted)
			lowHealth->play();
	}
	else if (sound == GameSound::GameOver)
		gameOver->play();
}
