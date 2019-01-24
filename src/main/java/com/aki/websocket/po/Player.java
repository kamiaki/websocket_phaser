package com.aki.websocket.po;

public class Player {
    public Player(int playerID, String name, int x, int y) {
        this.playerID = playerID;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    /**
     * playerID : 0
     * name :
     * x : 0
     * y : 0
     */

    private int playerID;
    private String name;
    private int x;
    private int y;

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
