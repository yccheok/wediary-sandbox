package com.yocto.keyboard_bottom_sheet_integration;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class BackgroundPickerLinearLayout extends LinearLayout {
    public BackgroundPickerLinearLayout(Context context) {
        super(context);

        init();
    }

    public BackgroundPickerLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public BackgroundPickerLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public BackgroundPickerLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.background_picker, this);

        setOrientation(VERTICAL);
    }
}
