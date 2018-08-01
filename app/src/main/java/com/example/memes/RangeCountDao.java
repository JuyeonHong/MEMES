package com.example.memes;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RangeCountDao {
    @Insert
    public void addToday(RangeCount rangeCount);

    @Query("select * from range_count")
    public List<RangeCount> getAll();

    @Update
    public void updateCount(RangeCount rangeCount);

    @Query("select * from range_count where date = :date")
    public RangeCount getRecordByDate(int date);

    @Delete
    public void deleteRecord(RangeCount rangeCount);
}
