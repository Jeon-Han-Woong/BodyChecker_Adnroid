package org.ict.bodychecker.ValueObject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoalVO {

    @SerializedName("gno")
    @Expose
    private Integer gno;
    @SerializedName("gtitle")
    @Expose
    private String gtitle;
    @SerializedName("gcontent")
    @Expose
    private String gcontent;
    @SerializedName("set_date")
    @Expose
    private String setDate;
    @SerializedName("fin_date")
    @Expose
    private String finDate;
    @SerializedName("gsts")
    @Expose
    private Integer gsts;
    @SerializedName("mno")
    @Expose
    private Integer mno;

    public Integer getGno() {
        return gno;
    }

    public void setGno(Integer gno) {
        this.gno = gno;
    }

    public String getGtitle() {
        return gtitle;
    }

    public void setGtitle(String gtitle) {
        this.gtitle = gtitle;
    }

    public String getGcontent() {
        return gcontent;
    }

    public void setGcontent(String gcontent) {
        this.gcontent = gcontent;
    }

    public String getSetDate() {
        return setDate;
    }

    public void setSetDate(String setDate) {
        this.setDate = setDate;
    }

    public String getFinDate() {
        return finDate;
    }

    public void setFinDate(String finDate) {
        this.finDate = finDate;
    }

    public Integer getGsts() {
        return gsts;
    }

    public void setGsts(Integer gsts) {
        this.gsts = gsts;
    }

    public Integer getMno() {
        return mno;
    }

    public void setMno(Integer mno) {
        this.mno = mno;
    }

}