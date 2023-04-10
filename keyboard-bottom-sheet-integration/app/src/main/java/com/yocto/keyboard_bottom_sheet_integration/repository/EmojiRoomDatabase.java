package com.yocto.keyboard_bottom_sheet_integration.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.yocto.keyboard_bottom_sheet_integration.model.Emoji;

@Database(entities = {Emoji.class}, version = 1)
public abstract class EmojiRoomDatabase extends RoomDatabase {
    public abstract EmojiDao emojiDao();

    private volatile static EmojiRoomDatabase INSTANCE;

    private static final String NAME = "emoji-export";

    public static EmojiRoomDatabase instance(Context context) {
        if (INSTANCE == null) {
            synchronized (EmojiRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context,
                            EmojiRoomDatabase.class,
                            NAME
                    )
                    .createFromAsset(NAME)
                    .allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
