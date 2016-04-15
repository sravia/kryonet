package com.engine.model;

public class NPC {

    private String accountId;
    private long npcId;
    private long x;
    private long y;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public long getNpcId() {
        return npcId;
    }

    public void setNpcId(long npcId) {
        this.npcId = npcId;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }
}
