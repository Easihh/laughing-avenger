package com.zelda.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZoneLoader {
    
    private static volatile Map<String,Zone> zoneMapByName=new ConcurrentHashMap<String,Zone>();
    private static ZoneLoader zLoader = null;
    
    private ZoneLoader(){
        loadAllZoneFromFile();
    }
    
    private void loadAllZoneFromFile() {
        String zoneName="Zone1";
        Zone firstZone=new Zone(zoneName);
        zoneMapByName.put(zoneName, firstZone);
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
