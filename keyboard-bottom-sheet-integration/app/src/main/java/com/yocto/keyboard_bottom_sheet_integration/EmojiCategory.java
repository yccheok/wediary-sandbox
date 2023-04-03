package com.yocto.keyboard_bottom_sheet_integration;

public enum EmojiCategory {
    People(R.drawable.emoji_ios_category_people, 1),
    Nature(R.drawable.emoji_ios_category_nature, 2),
    Food(R.drawable.emoji_ios_category_food, 3),
    Activity(R.drawable.emoji_ios_category_activity, 4),
    Travel(R.drawable.emoji_ios_category_travel, 5),
    Objects(R.drawable.emoji_ios_category_objects, 6),
    Symbols(R.drawable.emoji_ios_category_symbols, 7),
    Flags(R.drawable.emoji_ios_category_flags, 8),
    Recent(R.drawable.emoji_recent, 9);

    EmojiCategory(int resourceId, int code) {
        this.resourceId = resourceId;
        this.code = code;
    }

    public final int resourceId;
    public final int code;
}
