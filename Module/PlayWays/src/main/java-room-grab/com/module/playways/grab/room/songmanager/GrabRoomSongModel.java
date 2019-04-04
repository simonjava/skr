package com.module.playways.grab.room.songmanager;

import java.io.Serializable;

public class GrabRoomSongModel implements Serializable {
    /**
     * itemName : string
     * owner : string
     * playbookItemID : 0
     * roundSeq : 0
     */

    private String itemName;
    private String owner;
    private int itemID;
    private int roundSeq;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getRoundSeq() {
        return roundSeq;
    }

    public void setRoundSeq(int roundSeq) {
        this.roundSeq = roundSeq;
    }
}
