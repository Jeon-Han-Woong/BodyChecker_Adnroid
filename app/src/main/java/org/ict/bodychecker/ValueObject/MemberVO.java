package org.ict.bodychecker.ValueObject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemberVO {

    @SerializedName("mno")
    @Expose
    private int mno;

    @SerializedName("mid")
    @Expose
    private String mid;

    @SerializedName("pwd")
    @Expose
    private String pwd;

    @SerializedName("mname")
    @Expose
    private String mname;

    @SerializedName("gender")
    @Expose
    private int gender;

    @SerializedName("height")
    @Expose
    private int height;

    @SerializedName("weight")
    @Expose
    private int weight;

    @SerializedName("bmi")
    @Expose
    private int bmi;

    @SerializedName("birthday")
    @Expose
    private String birthday;

    @SerializedName("regdate")
    @Expose
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
