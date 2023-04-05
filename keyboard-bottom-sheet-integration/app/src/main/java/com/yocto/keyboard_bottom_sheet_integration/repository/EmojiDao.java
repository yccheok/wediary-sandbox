package com.yocto.keyboard_bottom_sheet_integration.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.yocto.keyboard_bottom_sheet_integration.model.Emoji;

import java.util.List;

@Dao
public interface EmojiDao {
    @Query("SELECT * FROM emoji WHERE category = :category AND selected = 1 order by id")
    public abstract List<Emoji> getEmojis(int category);
}
