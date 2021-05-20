package org.ict.bodychecker.ValueObject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailyVO {

    @SerializedName("dno")
    @Expose
    private int dno;

    @SerializedName("walk")
    @Expose
    private int walk;

    @SerializedName("water")
    @Expose
    private int water;

    @SerializedName("ddate")
    @Expose
    private String ddate;

    @SerializedName("mno")
    @Expose
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
