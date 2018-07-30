package com.example.memes;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {RangeCount.class}, version = 1)
public abstract class MemesDatabase extends RoomDatabase {
    public abstract RangeCountDao rangeCountDao();
}
