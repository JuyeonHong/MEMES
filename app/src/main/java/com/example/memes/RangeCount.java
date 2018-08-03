package com.example.memes;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "range_count")
public class RangeCount {
    @PrimaryKey
    private int date;

    private int range0_15;
    private int range15_30;
    private int range30_45;
    private int range45_60;
    private int range60_90;
    private int range90over;
    private int sumOfAll;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getRange0_15() {
        return range0_15;
    }

    public void setRange0_15(int range0_15) {
        this.range0_15 = range0_15;
    }

    public int getRange15_30() {
        return range15_30;
    }

    public void setRange15_30(int range15_30) {
        this.range15_30 = range15_30;
    }

    public int getRange30_45() {
        return range30_45;
    }

    public void setRange30_45(int range30_45) {
        this.range30_45 = range30_45;
    }

    public int getRange45_60() {
        return range45_60;
    }

    public void setRange45_60(int range45_60) {
        this.range45_60 = range45_60;
    }

    public int getRange60_90() {
        return range60_90;
    }

    public void setRange60_90(int range60_90) {
        this.range60_90 = range60_90;
    }

    public int getRange90over() {
        return range90over;
    }

    public void setRange90over(int range90over) {
        this.range90over = range90over;
    }

    public int getSumOfAll() {
        return sumOfAll;
    }

    public void setSumOfAll(int sumOfAll) {
        this.sumOfAll = sumOfAll;
    }
}
