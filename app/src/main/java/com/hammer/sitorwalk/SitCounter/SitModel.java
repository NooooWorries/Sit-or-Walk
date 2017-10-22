package com.hammer.sitorwalk.SitCounter;

/**
 * Created by Cheng on 22/10/17.
 */

public class SitModel {
    private int id;
    private String date;
    private int sit;

    public static final String TABLE="Sit";

    public static final String KEY_ID="id";
    public static final String KEY_DATE="date";
    public static final String KEY_SIT="sit";

    public SitModel() {

    }

    public SitModel(String date, int sit) {
        this.date = date;
        this.sit = sit;
    }

    public SitModel(int id, String date, int sit) {
        this.id = id;
        this.date = date;
        this.sit = sit;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getSit() {
        return sit;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSit(int sit) {
        this.sit = sit;
    }
}
