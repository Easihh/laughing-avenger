package com.zelda.server;

import java.io.InputStream;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zelda.common.GameObject;
import com.zelda.common.Quadtree;
import com.zelda.server.entity.Tile;

import static com.zelda.common.Constants.Size.MAX_TILE_HEIGHT;
import static com.zelda.common.Constants.Size.MAX_TILE_WIDTH;;

public class Zone {

    private String zoneName;
    private final int ZONE_MAX_HEIGHT = 512;
    private final int ZONE_MAX_WIDTH = 512;
    private final int ZONE_START_X = 0;
    private final int ZONE_START_Y = 0;
    private Quadtree<GameObject> staticObjQtree;

    private final Logger LOG = LoggerFactory.getLogger(Zone.class);

    public Zone(String zName) {
        staticObjQtree = new Quadtree<>(ZONE_START_X, ZONE_START_Y, ZONE_MAX_WIDTH, ZONE_MAX_HEIGHT, 1);
        zoneName = zName;
        readZoneFromFile();
    }

    private void readZoneFromFile() {
        InputStream is = ZoneLoader.class.getResourceAsStream("/" + zoneName + ".csv");
        Scanner scan = new Scanner(is);
        int zoneStartX = 0;
        int zoneStartY = 0;
        int insertCount = 0;
        while (scan.hasNextLine()) {
            String lineValue = scan.nextLine();
            String[] arr = lineValue.split(",", 0);
            for (int i = 0; i < arr.length; i++) {
                int identifier = Integer.parseInt(arr[i]);
                switch (identifier) {
                case -1:
                    break;
                case 0:
                    Tile tile = new Tile(zoneStartX, zoneStartY);
                    System.out.println("X:" + zoneStartX + " Y:" + zoneStartY);
                    staticObjQtree.insert(tile);
                    insertCount++;
                    break;
                default:
                    LOG.error("Invalid identifier:" + identifier);
                }
                zoneStartX += MAX_TILE_WIDTH;
            }
            zoneStartY += MAX_TILE_HEIGHT;
            zoneStartX = 0;
        }
        System.out.println("Inserted:" + insertCount);
        scan.close();
    }
    
    public Quadtree<GameObject> getStaticObjQtree() {
        return staticObjQtree;
    }
}
