package com.yocto.keyboard_bottom_sheet_integration;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class EmojiPicker extends LinearLayout {
    private final PickerListener pickerListener;

    public EmojiPicker(Context context, PickerListener pickerListener) {
        super(context);

        this.pickerListener = pickerListener;

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.emoji_picker, this);

        setOrientation(VERTICAL);

        findViewById(R.id.image_button_0).setOnClickListener(view -> pickerListener.onPickerClosed());
    }
}
