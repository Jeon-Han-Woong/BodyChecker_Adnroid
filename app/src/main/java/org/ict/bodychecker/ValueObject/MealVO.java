package org.ict.bodychecker.ValueObject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class MealVO {

    @SerializedName("fno")
    @Expose
    private int fno;

    @SerializedName("fname")
    @Expose
    private String fname;

    @SerializedName("fkcal")
    @Expose
    private int fkcal;

    @SerializedName("ftime")
    @Expose
    private String ftime;

    @SerializedName("fdate")
    @Expose
    private String fdate;

    @SerializedName("mno")
    @Expose
    private int mno;

    public int getFno() {
        return fno;
    }
    public void setFno(int fno) {
        this.fno = fno;
    }

    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getFkcal() {
        return fkcal;
    }
    public void setFkcal(int fkcal) {
        this.fkcal = fkcal;
    }

    public String getFtime() {
        return ftime;
    }
    public void setFtime(String ftime) {
        this.ftime = ftime;
    }

    public String getFdate() {
        return fdate;
    }
    public void setFdate(String fdate) {
        this.fdate = fdate;
    }

    public int getMno() {
        return mno;
    }
    public void setMno(int mno) {
        this.mno = mno;
    }
}