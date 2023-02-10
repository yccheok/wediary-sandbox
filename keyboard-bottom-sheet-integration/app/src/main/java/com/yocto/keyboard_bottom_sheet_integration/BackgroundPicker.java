package com.yocto.keyboard_bottom_sheet_integration;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class BackgroundPicker extends LinearLayout {
    private final PickerListener pickerListener;

    public BackgroundPicker(Context context, PickerListener pickerListener) {
        super(context);

        this.pickerListener = pickerListener;

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.background_picker, this);

        setOrientation(VERTICAL);

        findViewById(R.id.image_button_0).setOnClickListener(view -> pickerListener.onPickerClosed());
    }
}
