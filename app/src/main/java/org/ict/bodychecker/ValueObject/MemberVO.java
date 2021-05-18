package org.ict.bodychecker.ValueObject;

public class MemberVO {
    private int mno;
    private String mid;
    private String pwd;
    private String mname;
    private int gender;
    private int height;
    private int weight;
    private int bmi;
    private String birthday;
    private String regdate;

    public int getMno() {
        return mno;
    }
    public void setMno(int mno) {
        this.mno = mno;
    }

    public String getMid() {
        return mid;
    }
    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMname() {
        return mname;
    }
    public void setMname(String mname) {
        this.mname = mname;
    }

    public int getGender() {
        return gender;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getBmi() {
        return bmi;
    }
    public void setBmi(int bmi) {
        this.bmi = bmi;
    }

    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRegdate() {
        return regdate;
    }
    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }
}
