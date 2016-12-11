package com.zelda.game;

public class PositionUpdater {
    
    private int serverX;
    private int serverY;
    
    public PositionUpdater(int x,int y){
        serverX=x;
        serverY=y;
    }
    
    public int getserverX() {
        return serverX;
    }

    public int getserverY() {
        return serverY;
    }
    
    public int getNextXMovement(int speed){
        if(speed >serverX){
            return serverX;
        }
         return speed;
    }
    
    public int getNextYMovement(int speed){
        if(speed >serverY){
            return serverY;
        }
         return speed;
    }
    
    public void decrementX(int movement){
        serverX-=movement;
        if(serverX<0){
            serverX=0;
        }
    }
    
    public void decrementY(int movement){
        serverY-=movement;
        if(serverY<0){
            serverY=0;
        }
    }
}
