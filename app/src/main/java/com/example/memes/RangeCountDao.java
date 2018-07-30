package com.example.memes;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RangeCountDao {
    @Insert
    public void addToday(RangeCount rangeCount);

    @Query("select * from range_count")
    public List<RangeCount> getAll();
}
