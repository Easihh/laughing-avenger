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
#include "Monster\Stalfos.h"
#include "Monster\Goriya.h"
#include "Monster\Trap.h"
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
	float x = lastWorldXIndex*Global::TileWidth;
	float y = lastWorldYIndex*Global::TileHeight;
	Point pt(x, y + Global::inventoryHeight);
	switch(tileType){
	case -1:
	//no tile;
	break;
	case Identifier::Sand_ID:
	tile = std::make_shared<Tile>(pt, false, TileType::Sand);
	break;
	case Identifier::GreenTree_ID:
	tile = std::make_shared<Tile>(pt, true, TileType::GreenTree);
	break;
	case Identifier::RedOctorokMonster:
	tile = std::make_shared<Octorok>(pt, false);
	break;
	case Identifier::BlackTile_ID:
	tile = std::make_shared<Tile>(pt, false, TileType::BlackTile);
	break;
	case Identifier::ShopMarker_ID:
	tile = std::make_shared<ShopMarker>(pt);
	break;
	case Identifier::BrownBlock1_ID:
	tile = std::make_shared<Tile>(pt, true, TileType::BrownBlockType1);
	break;
	case Identifier::WoodSword:
	tile = std::make_shared<WoodSwordPickUp>(pt);
	break;
	case Identifier::FlameObj:
	tile = std::make_shared<Flame>(pt, true);
	break;
	case Identifier::BrownBlock2_ID:
	tile = std::make_shared<Tile>(pt, true, TileType::BrownBlockType2);
	break;
	case Identifier::ItemShopBomb:
	tile = std::make_shared<ShopBomb>(pt);
	break;
	case Identifier::ItemShopCandle:
	tile = std::make_shared<ShopCandle>(pt);
	break;
	case Identifier::ItemShopFood:
	//tile = std::make_shared<ShopFood>(pt);
	//objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::SecretShopPotion:
	tile = std::make_shared<PotionPickUp>(pt);
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
	break;
	case Identifier::TeleportDungeon:
	tile = std::make_shared<DungeonMarker>(pt);
	break;
	case Identifier::ExitDungeon:
	tile = std::make_shared<LeaveDungeonMarker>(pt);
	break;
	case Identifier::ExitShop:
	tile = std::make_shared<LeaveShopMarker>(pt);
	break;
	case Identifier::SecretGreenTree:
	tile = std::make_shared<SecretTree>(pt);
	break;
	case Identifier::HeartContainerItem:
	tile = std::make_shared<HeartContainer>(pt);
	break;
	case Identifier::Merchant:
	tile = std::make_shared<NPC>(pt, NpcType::Merchant);
	break;
	case Identifier::OldMan:
	tile = std::make_shared<NPC>(pt,NpcType::OldMan);
	break;
	case Identifier::DungeonTile1:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile1);
	break;
	case Identifier::DungeonTile2:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile2);
	break;
	case Identifier::DungeonTile3:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile3);
	break;
	case Identifier::DungeonTile4:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile4);
	break;
	case Identifier::DungeonTile5:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile5);
	break;
	case Identifier::DungeonTile6:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile6);
	break;
	case Identifier::DungeonTile7:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile7);
	break;
	case Identifier::DungeonTile8:
	//upper part of North Side door
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile8);
	break;
	case Identifier::DungeonTile9:
	//blocked dungeon right door part
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile9);
	break;
	case Identifier::DungeonTile10:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile10);
	break;
	case Identifier::DungeonTile11:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile11);
	break;
	case Identifier::DungeonTile12:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile12);
	break;
	case Identifier::DungeonTile13:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile13);
	break;
	case Identifier::DungeonTile14:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile14);
	break;
	case Identifier::DungeonTile15:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile15);
	break;
	case Identifier::DungeonTile16:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile16);
	break;
	case Identifier::DungeonTile17:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile17);
	break;
	case Identifier::DungeonTile18:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile18);
	break;
	case Identifier::DungeonTile19:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile19);
	break;
	case Identifier::DungeonTile20:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile20);
	break;
	case Identifier::DungeonTile21:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile21);
	break;
	case Identifier::DungeonTile22:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile22);
	break;
	case Identifier::DungeonTile23:
	//blocked dungeon right door part
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile23);
	break;
	case Identifier::DungeonTile24:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile24);
	break;
	case Identifier::DungeonTile25:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile25);
	break;
	case Identifier::DungeonTile26:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile26);
	break;
	case Identifier::DungeonTile27:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile27);
	break;
	case Identifier::DungeonTile28:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile28);
	break;
	case Identifier::DungeonTile29:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile29);
	break;
	case Identifier::ArtifactRoomStair:
	tile = std::make_shared<Tile>(pt, false, TileType::ArtifactRoomStair);
	break;
	case Identifier::ArtifactRoomWall:
	tile = std::make_shared<Tile>(pt, true, TileType::ArtifactRoomWall);
	break;
	case Identifier::DungeonPushBlock:
	tile = std::make_shared<MoveableBlock>(pt);
	break;
	case Identifier::DungeonTile33:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile33);
	break;
	case Identifier::DungeonTile34:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile34);
	break;
	case Identifier::DungeonTile35:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile35);
	break;
	case Identifier::DungeonTile36:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile36);
	break;
	case Identifier::DungeonTile37:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile37);
	break;
	case Identifier::DungeonTile38:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile38);
	break;
	case Identifier::DungeonTile39:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile39);
	break;
	case Identifier::DungeonTile40:
	//upper part of South Side door
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile40);
	break;
	case Identifier::DungeonTile41:
	//blocked dungeon right door part
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile41);
	break;
	case Identifier::DungeonTile42:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile42);
	break;
	case Identifier::DungeonTile43:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile43);
	break;
	case Identifier::DungeonTile44:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile44);
	break;
	case Identifier::DungeonTile45:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile45);
	break;
	case Identifier::DungeonTile46:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile46);
	break;
	case Identifier::DungeonTile47:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile47);
	break;
	case Identifier::DungeonTile48:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile48);
	break;
	case Identifier::DungeonTile49:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile49);
	break;
	case Identifier::DungeonTile50:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile50);
	break;
	case Identifier::DungeonTile51:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile51);
	break;
	case Identifier::DungeonTile52:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile52);
	break;
	case Identifier::DungeonTile53:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile53);
	break;
	case Identifier::DungeonTile54:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile54);
	break;
	case Identifier::DungeonTile55:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile55);
	break;
	case Identifier::DungeonTile56:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile56);
	break;
	case Identifier::DungeonTile57:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile57);
	break;
	case Identifier::DungeonTile58:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile58);
	break;
	case Identifier::DungeonTile59:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile59);
	break;
	case Identifier::DungeonTile60:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile60);
	break;
	case Identifier::DungeonTile61:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile61);
	break;
	case Identifier::DungeonTile62:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile62);
	break;
	case Identifier::DungeonTile63:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile63);
	break;
	case Identifier::DungeonTile64:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile64);
	break;
	case Identifier::DungeonTile65:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile65);
	break;
	case Identifier::DungeonTile66:
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile66);
	break;
	case Identifier::DungeonTile67:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile67);
	objectVector[vectorXindex][vectorYindex].push_back(tile);
	break;
	case Identifier::DungeonTile68:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile68);
	break;
	case Identifier::DungeonTile69:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile69);
	break;
	case Identifier::DungeonTile70:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile70);
	break;
	case Identifier::DungeonTile71:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile71);
	break;
	case Identifier::DungeonTile72:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile72);
	break;
	case Identifier::ToArtifactRoom:
	tile = std::make_shared<TeleportToArtifactRoom>(pt);
	break;
	case Identifier::DungeonTile74:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile74);
	break;
	case Identifier::DungeonTile75:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile75);
	break;
	case Identifier::FromArtifactRoom:
	tile = std::make_shared<TeleportFromArtifactRoom>(pt);
	break;
	case Identifier::DungeonTile77:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile77);
	break;
	case Identifier::DungeonTile78:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile78);
	break;
	case Identifier::DungeonTile79:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile79);
	break;
	case Identifier::DungeonTile80:
	//upper part of East Side door
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile80);
	break;
	case Identifier::DungeonTile81:
	//upper part of West Side door
	tile = std::make_shared<Tile>(pt, false, TileType::DungeonTile81);
	break;
	case Identifier::DungeonTile82:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile82);
	break;
	case Identifier::DungeonTile83:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile83);
	break;
	case Identifier::DungeonTile84:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile84);
	break;
	case Identifier::RedDungeonTile:
	tile = std::make_shared<Tile>(pt, true, TileType::RedDungeonTile);
	break;
	case Identifier::DungeonTile86:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile86);
	break;
	case Identifier::DungeonTile87:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile87);
	break;
	case Identifier::DungeonTile88:
	tile = std::make_shared<Tile>(pt, true, TileType::DungeonTile88);
	break;
	case Identifier::OverworldTile1:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile1);
	break;
	case Identifier::OverworldTile2:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile2);
	break;
	case Identifier::OverworldTile3:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile3);
	break;
	case Identifier::OverworldTile4:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile4);
	break;
	case Identifier::OverworldTile5:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile5);
	break;
	case Identifier::OverworldTile6:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile6);
	break;
	case Identifier::OverworldTile7:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile7);
	break;
	case Identifier::GreenArmos:
	tile = std::make_shared<Tile>(pt, true, TileType::GreenArmosStatue);
	break;
	case Identifier::OverworldTile8:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile8);
	break;
	case Identifier::OverworldTile9:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile9);
	break;
	case Identifier::OverworldTile10:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile10);
	break;
	case Identifier::OverworldTile11:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile11);
	break;
	case Identifier::OverworldTile12:
	tile = std::make_shared<Tile>(pt, true, TileType::OverworldTile12);
	break;
	case Identifier::BlueBat:
	tile = std::make_shared<Keese>(pt, true);
	break;
	case Identifier::Map:
	tile = std::make_shared<DungeonMap>(pt);
	break;
	case Identifier::BlackTileBlocker:
	tile = std::make_shared<Tile>(pt, true,TileType::BlackTile);
	break;
	case Identifier::DungeonCompass:
	tile = std::make_shared<Compass>(pt);
	break;
	case Identifier::DungeonTriforce:
	tile = std::make_shared<Triforce>(pt);
	break;
	case Identifier::BowItem:
	tile = std::make_shared<BowPickUp>(pt);
	break;
	case Identifier::ItemShopArrow:
	tile = std::make_shared<ShopArrow>(pt);
	break;
	case Identifier::ItemShopKey:
	tile = std::make_shared<ShopKey>(pt);
	break;
	case Identifier::GelMonster:
	tile = std::make_shared<Gel>(pt,true);
	break;
	case Identifier::StalfosMonster:
	tile = std::make_shared<Stalfos>(pt, true);
	break;
	case Identifier::RedGoriyaMonster:
	tile = std::make_shared<Goriya>(pt, true);
	break;
	case Identifier::DungeonTrap:
	tile = std::make_shared<Trap>(pt, true);
	break;
	}
	objectVector[vectorXindex][vectorYindex].push_back(tile);
}