#include "WorldMap.h"
#include "Static.h"
#include "Tile.h"
WorldMap::WorldMap(){
	lastWorldXIndex = 0;
	lastWorldYIndex = 0;
	loadMap();
}
WorldMap::~WorldMap(){

}
void WorldMap::loadMap(){
	in.open("Map/Zelda-Worldmap_Layer 1.csv");
	while (!in.eof()){
		std::getline(in, line, '\n');
		boost::split(strs, line, boost::is_any_of(","));
		for (std::vector<std::string>::iterator it = strs.begin(); it < strs.end(); it++){
			std::cout << "Value:" << *it << std::endl;
			Tile* backgroundTile=new Tile(lastWorldXIndex * TileWidth, lastWorldYIndex * TileHeight);
			worldLayer1[lastWorldYIndex][lastWorldXIndex] = backgroundTile;
			lastWorldXIndex++;
		}
		lastWorldYIndex++;
		lastWorldXIndex = 0;
	}
	in.close();
}
void WorldMap::update(sf::RenderWindow& mainWindow){
	timeSinceLastUpdate += timerClock.restart();
	fpsTimer += fpsClock.restart();
	if (fpsTimer.asMilliseconds() >= FPS_REFRESH_RATE){
		std::stringstream title;
		title << Static::GAME_TITLE << "FPS:" << fpsCounter;
		mainWindow.setTitle(title.str());
		fpsCounter = 0;
		fpsTimer = sf::Time::Zero;
	}
	if (timeSinceLastUpdate.asMilliseconds() >= 1667){
		mainWindow.clear(sf::Color::Black);
		fpsCounter++;
		timeSinceLastUpdate -= timePerFrame;
		for (int i = 0; i < WorldRows; i++){
			for (int j = 0; j < WorldColumns; j++){
				worldLayer1[i][j]->update();
				worldLayer1[i][j]->draw(mainWindow);
			}
		}
		sf::Font font;
		std::stringstream position;
		position << "X:" << std::endl << "Y:";
		font.loadFromFile("arial.ttf");
		sf::Text txt(position.str(), font);
		txt.setColor(sf::Color::Red);
		txt.setPosition(400, 400);
		txt.setCharacterSize(24);
		mainWindow.draw(txt);
		mainWindow.display();
	}
}