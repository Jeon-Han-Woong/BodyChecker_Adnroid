package org.ict.bodychecker.ValueObject;

//public class ExerciseVO {
//
//    private String exerciseTitle;
//    private int exerciseMin;
//    private int exerciseKcal;
//
//    public String getExerciseTitle() {
//        return exerciseTitle;
//    }
//
//    public void setExerciseTitle(String exerciseTitle) {
//        this.exerciseTitle = exerciseTitle;
//    }
//
//    public int getExerciseMin() {
//        return exerciseMin;
//    }
//
//    public void setExerciseMin(int exerciseMin) {
//        this.exerciseMin = exerciseMin;
//    }
//
//    public int getExerciseKcal() {
//        return exerciseKcal;
//    }
//
//    public void setExerciseKcal(int exerciseKcal) {
//        this.exerciseKcal = exerciseKcal;
//    }
//}

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExerciseVO {

    @SerializedName("eno")
    @Expose
    private Integer eno;
    @SerializedName("ename")
    @Expose
    private String ename;
    @SerializedName("ekcal")
    @Expose
    private Integer ekcal;
    @SerializedName("etime")
    @Expose
    private Integer etime;
    @SerializedName("edate")
    @Expose
    private String edate;
    @SerializedName("mno")
    @Expose
    private Integer mno;

    public Integer getEno() {
        return eno;
    }

    public void setEno(Integer eno) {
        this.eno = eno;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public Integer getEkcal() {
        return ekcal;
    }

    public void setEkcal(Integer ekcal) {
        this.ekcal = ekcal;
    }

    public Integer getEtime() {
        return etime;
    }

    public void setEtime(Integer etime) {
        this.etime = etime;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public Integer getMno() {
        return mno;
    }

    public void setMno(Integer mno) {
        this.mno = mno;
    }

}
