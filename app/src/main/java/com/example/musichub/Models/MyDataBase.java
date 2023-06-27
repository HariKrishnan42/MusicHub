package com.example.musichub.Models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.musichub.Interfaces.UserDao;

@Database(entities = {UserAccount.class}, version = 1)
public abstract class MyDataBase extends RoomDatabase {

    public abstract UserDao getDao();

    public static MyDataBase instance;

    public static MyDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, MyDataBase.class, "Music Hub")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
