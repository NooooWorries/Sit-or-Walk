package com.hammer.sitorwalk.StepCounter;

import java.util.Date;

/**
 * Created by Cheng on 14/10/17.
 */

public class HistoryModel {
    private int id;
    private String date;
    private int steps;

    public static final String TABLE="History";

    public static final String KEY_ID="id";
    public static final String KEY_DATE="date";
    public static final String KEY_STEPS="steps";

    public HistoryModel() {

    }

    public HistoryModel(String date, int steps) {
        this.date = date;
        this.steps = steps;
    }

    public HistoryModel(int id, String date, int steps) {
        this.id = id;
        this.date = date;
        this.steps = steps;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getSteps() {
        return steps;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}
