package com.zelda.game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.zelda.common.Constants.Size;


public class ZoneLoader {
    
    private static volatile Map<String,Zone> zoneMapByName=new ConcurrentHashMap<String,Zone>();
    private static ZoneLoader zLoader = null;
    
    private ZoneLoader(){
        loadAllZoneFromFile();
    }
    
    private void loadAllZoneFromFile() {
        String zoneName = "Zone1";
        Zone zone = new Zone(zoneName, 0, 0);
        zoneMapByName.put(zoneName, zone);

        zoneName = "Zone2";
        zone = new Zone(zoneName, 512 * Size.WORLD_SCALE_X, 0);
        zoneMapByName.put(zoneName, zone);
    }

    public static ZoneLoader getInstance() {
        if (zLoader == null) {
            zLoader = new ZoneLoader();
        }
        return zLoader;
    }
        
    public Zone getZoneByName(String zName){
        return zoneMapByName.get(zName);
    }
}
