package com.yocto.keyboard_bottom_sheet_integration;

import android.content.Context;
import android.widget.LinearLayout;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class EmojiPicker extends LinearLayout {
    private final PickerListener pickerListener;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    public EmojiPicker(Context context, PickerListener pickerListener) {
        super(context);

        this.pickerListener = pickerListener;

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.emoji_picker, this);

        setOrientation(VERTICAL);

        this.tabLayout = findViewById(R.id.tab_layout);
        this.viewPager = findViewById(R.id.view_pager);

        EmojiPickerAdapter emojiPickerAdapter = new EmojiPickerAdapter(getContext());
        this.viewPager.setAdapter(emojiPickerAdapter);
        this.viewPager.setOffscreenPageLimit(1);

        LinearLayout tabsContainer = (LinearLayout) tabLayout.getChildAt(0);

        new TabLayoutMediator(tabLayout, viewPager, true, false,
                (tab, position) -> {
                    tab.setCustomView(R.layout.emoji_category_tab);

                    tab.setIcon(EmojiCategory.values()[position].resourceId);
                }
        ).attach();


        for (int i = 0, ei = tabLayout.getTabCount(); i < ei; i++) {
            LinearLayout item = (LinearLayout) tabsContainer.getChildAt(i);

            item.setBackgroundResource(0);
        }
    }
}
