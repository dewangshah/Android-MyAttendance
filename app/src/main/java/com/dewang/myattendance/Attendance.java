package com.dewang.myattendance;

/**
 * Created by Dewang on 3/3/2017.
 */

public class Attendance {


    private int id,agg,dwm,dwmt,hmi,hmit,ml,mlt,pds,pdst;
    private String name,stud_id,image;

    public Attendance(int id,String name,int dwm,int dwmt,int hmi,int hmit,int ml,int mlt,int pds,int pdst,String stud_id,String image,int agg){

        this.setId(id);
        this.setName(name);
        this.setDwm(dwm);
        this.setDwmt(dwmt);
        this.setHmi(hmi);
        this.setHmit(hmit);
        this.setMl(ml);
        this.setMlt(mlt);
        this.setPds(pds);
        this.setPdst(pdst);
        this.setStud_id(stud_id);
        this.setImageURL(image);
        this.setAgg(agg);
    }


    public Attendance(int id,String name)
    {
        this.setId(id);
        this.setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAgg() {
        return agg;
    }

    public void setAgg(int agg) {
        this.agg=agg;
    }

    public int getDwm() {
        return dwm;
    }

    public void setDwm(int dwm) {
        this.dwm = dwm;
    }

    public int getDwmt() {
        return dwmt;
    }

    public void setDwmt(int dwmt) {
        this.dwmt = dwmt;
    }

    public int getHmi() {
        return hmi;
    }

    public void setHmi(int hmi) {
        this.hmi = hmi;
    }

    public int getHmit() {
        return hmit;
    }

    public void setHmit(int hmit) {
        this.hmit = hmit;
    }

    public int getMl() {
        return ml;
    }

    public void setMl(int ml) {
        this.ml = ml;
    }

    public int getMlt() {
        return mlt;
    }

    public void setMlt(int mlt) {
        this.mlt = mlt;
    }

    public int getPds() {
        return pds;
    }

    public void setPds(int pds) {
        this.pds = pds;
    }

    public int getPdst() {
        return pdst;
    }

    public void setPdst(int pdst) {
        this.pdst = pdst;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStud_id() {
        return stud_id;
    }

    public void setStud_id(String stud_id) {
        this.stud_id = stud_id;
    }

    public String getImageURL() {
        return image;
    }

    public void setImageURL(String image) {
        this.image = image;
    }
}