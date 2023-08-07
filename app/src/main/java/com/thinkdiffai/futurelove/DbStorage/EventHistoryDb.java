package com.thinkdiffai.futurelove.DbStorage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.thinkdiffai.futurelove.model.EventHomeDto;


@Database(entities = {EventHomeDto.class}, version = 1)
public abstract class EventHistoryDb extends RoomDatabase {
    private static final String DATABASE_NAME = "event_history.db";

    private static EventHistoryDb instance;
    public static synchronized EventHistoryDb getInstance(Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(), EventHistoryDb.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
    public abstract EventHistoryDao eventHistoryDao();
}
