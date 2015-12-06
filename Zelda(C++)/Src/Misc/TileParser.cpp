#include "Misc\TileParser.h"
#include "Utility\Static.h"
#include "Type\Identifier.h"
#include "Monster\Octorok.h"
#include "Type\TileType.h"
#include "Misc\Marker\ShopMarker.h"
#include "Item\WoodSwordPickUp.h"
#include "Misc\Flame.h"
#include "Item\ThrownArrow.h"
#include "Item\ThrownBomb.h"
#include "Item\BombEffect.h"
#include "Misc\ShopBomb.h"
#include "Misc\ShopRupeeDisplayer.h"
#include "Misc\CandleFlame.h"
#include "Misc\Marker\DungeonMarker.h"
#include "Misc\Marker\LeaveDungeonMarker.h"
#include "Misc\Marker\LeaveShopMarker.h"
#include "Misc\SecretTree.h"
#include "Item\HeartContainer.h"
#include "Misc\NPC.h"
#include "Monster\Armos.h"
#include "Monster\Keese.h"
#include "Monster\Gel.h"
#include "Misc\MoveableBlock.h"
#include "Misc\Marker\TeleportToArtifactRoom.h"
#include "Misc\Marker\TeleportFromArtifactRoom.h"
#include "Item\DungeonMap.h"
#include "Item\Compass.h"
#include "Item\Triforce.h"
#include "Item\BowPickUp.h"
#include "Misc\ShopArrow.h"
#include "Misc\ShopCandle.h"
#include "Misc\ShopKey.h"
#include "Item\PotionPickUp.h"
TileParser::TileParser() {}

void TileParser::createTile(int lastWorldXIndex, int lastWorldYIndex, int tileType, tripleVector& objectVector, int vectorXindex, int vectorYindex) {
	std::shared_ptr<GameObject> tile;
	float x = lastWorldXIndex*Global::TileWidth;
	float y = lastWorldYIndex*Global::TileHeight;
	Point pt(x, y + Global::inventoryHeight);
	switch(tileType){
	case -1:
	//no tile;
	break;
	case Identifier::Sand_ID:
	tile = std::make_shared<Tile>(pt, false, TileType::Sand);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::GreenTree_ID:
	tile = std::make_shared<Tile>(pt, true, TileType::GreenTree);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::RedOctorokMonster:
	tile = std::make_shared<Octorok>(pt, false);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::BlackTile_ID:
	tile = std::make_shared<Tile>(pt, false, TileType::BlackTile);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::ShopMarker_ID:
	tile = std::make_shared<ShopMarker>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::BrownBlock1_ID:
	tile = std::make_shared<Tile>(pt, true, TileType::BrownBlockType1);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::WoodSword:
	tile = std::make_shared<WoodSwordPickUp>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::FlameObj:
	tile = std::make_shared<Flame>(pt, true);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::BrownBlock2_ID:
	tile = std::make_shared<Tile>(pt, true, TileType::BrownBlockType2);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::ItemShopBomb:
	tile = std::make_shared<ShopBomb>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::ItemShopCandle:
	tile = std::make_shared<ShopCandle>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::ItemShopFood:
	//tile = std::make_shared<ShopFood>(pt);
	//objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::SecretShopPotion:
	tile = std::make_shared<PotionPickUp>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::ItemMagicalRod:
	//tile = std::make_shared<ShopMagicalRod>(pt);
	//objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::ItemFlute:
	//tile = std::make_shared<ShopMagicalRod>(pt);
	//objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::ShopDiamondDisplay:
	tile = std::make_shared<ShopRupeeDisplayer>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::TeleportDungeon:
	tile = std::make_shared<DungeonMarker>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::ExitDungeon:
	tile = std::make_shared<LeaveDungeonMarker>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::ExitShop:
	tile = std::make_shared<LeaveShopMarker>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::SecretGreenTree:
	tile = std::make_shared<SecretTree>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::HeartContainerItem:
	tile = std::make_shared<HeartContainer>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::Merchant:
	tile = std::make_shared<NPC>(pt, NpcType::Merchant);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::OldMan:
	tile = std::make_shared<NPC>(pt,NpcType::OldMan);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile1:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile1);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile2:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile2);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile3:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile3);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile4:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile4);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile5:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile5);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile6:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile6);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile7:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile7);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile8:
	//upper part of North Side door
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile8);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile9:
	//blocked dungeon right door part
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile9);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile10:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile10);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile11:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile11);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile12:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile12);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile13:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile13);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile14:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile14);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile15:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile15);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile16:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile16);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile17:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile17);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile18:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile18);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile19:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile19);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile20:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile20);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile21:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile21);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile22:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile22);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile23:
	//blocked dungeon right door part
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile23);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile24:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile24);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile25:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile25);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile26:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile26);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile27:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile27);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile28:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile28);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile29:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile29);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::ArtifactRoomStair:
	tile = std::make_shared<Tile>(pt, false, TileType::ArtifactRoomStair);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::ArtifactRoomWall:
		tile = std::make_shared<Tile>(pt, true, TileType::ArtifactRoomWall);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonPushBlock:
	tile = std::make_shared<MoveableBlock>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile33:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile33);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile34:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile34);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile35:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile35);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile36:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile36);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile37:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile37);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile38:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile38);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile39:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile39);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile40:
	//upper part of South Side door
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile40);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile41:
	//blocked dungeon right door part
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile41);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile42:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile42);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile43:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile43);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile44:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile44);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile45:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile45);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile46:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile46);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile47:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile47);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile48:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile48);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile49:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile49);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile50:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile50);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile51:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile51);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile52:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile52);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile53:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile53);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile54:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile54);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile55:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile55);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile56:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile56);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile57:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile57);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile58:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile58);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile59:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile59);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile60:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile60);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile61:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile61);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile62:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile62);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile63:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile63);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile64:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile64);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile65:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile65);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile66:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile66);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile67:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile67);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile68:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile68);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile69:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile69);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile70:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile70);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile71:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile71);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile72:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile72);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::ToArtifactRoom:
	tile = std::make_shared<TeleportToArtifactRoom>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile74:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile74);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile75:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile75);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::FromArtifactRoom:
	tile = std::make_shared<TeleportFromArtifactRoom>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile77:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile77);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile78:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile78);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile79:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile79);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile80:
	//upper part of East Side door
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile80);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile81:
	//upper part of West Side door
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile81);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile82:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile82);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile83:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile83);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile84:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile84);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::RedDungeonTile:
		tile = std::make_shared<Tile>(pt, true, TileType::RedDungeonTile);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile86:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile86);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile87:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile87);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile88:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile88);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::OverworldTile1:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile1);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::OverworldTile2:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile2);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::OverworldTile3:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile3);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::OverworldTile4:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile4);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::OverworldTile5:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile5);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::OverworldTile6:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile6);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::OverworldTile7:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile7);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::GreenArmos:
	tile = std::make_shared<Tile>(pt, true, TileType::GreenArmosStatue);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::OverworldTile8:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile8);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::OverworldTile9:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile9);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::OverworldTile10:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile10);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::OverworldTile11:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile11);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::OverworldTile12:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile12);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::BlueBat:
	tile = std::make_shared<Keese>(pt, true);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::Map:
	tile = std::make_shared<DungeonMap>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::BlackTileBlocker:
	tile = std::make_shared<Tile>(pt, true,TileType::BlackTile);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonCompass:
	tile = std::make_shared<Compass>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTriforce:
	tile = std::make_shared<Triforce>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::BowItem:
	tile = std::make_shared<BowPickUp>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::ItemShopArrow:
	tile = std::make_shared<ShopArrow>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::ItemShopKey:
	tile = std::make_shared<ShopKey>(pt);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::GelMonster:
	tile = std::make_shared<Gel>(pt,true);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	}
}