package org.ict.bodychecker.ValueObject;

import java.sql.Date;

public class MealVO {
    private int fno;
    private String fname;
    private int fkcal;
    private String ftime;
    private String fdate;

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
}