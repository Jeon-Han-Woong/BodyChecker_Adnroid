package org.ict.bodychecker.ValueObject;

public class DailyVO {
    private int dno;
    private int walk;
    private int water;
    private String ddate;
    private int mno;

    public int getDno() {
        return dno;
    }
    public void setDno(int dno) {
        this.dno = dno;
    }

    public int getWalk() {
        return walk;
    }
    public void setWalk(int walk) {
        this.walk = walk;
    }

    public int getWater() {
        return water;
    }
    public void setWater(int water) {
        this.water = water;
    }

    public String getDdate() {
        return ddate;
    }
    public void setDdate(String ddate) {
        this.ddate = ddate;
    }

    public int getMno() {
        return mno;
    }
    public void setMno(int mno) {
        this.mno = mno;
    }
}
