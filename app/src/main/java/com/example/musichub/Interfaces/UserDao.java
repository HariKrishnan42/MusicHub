package com.example.musichub.Interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.musichub.Models.UserAccount;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(UserAccount userAccount);

    @Update
    void update(UserAccount userAccount);

    @Delete
    void delete(UserAccount userAccount);

    @Query("SELECT * FROM `Music Hub`")
    List<UserAccount> getDetails();
}
