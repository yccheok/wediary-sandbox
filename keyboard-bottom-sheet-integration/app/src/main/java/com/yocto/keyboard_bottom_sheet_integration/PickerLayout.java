package com.yocto.keyboard_bottom_sheet_integration;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PickerLayout extends LinearLayout {
    private final Picker picker;
    private final PickerListener pickerListener;

    private TextView titleTextView;
    private Button closeButton;

    public PickerLayout(Context context, Picker picker, PickerListener pickerListener) {
        super(context);

        this.picker = picker;

        this.pickerListener = pickerListener;

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.picker_layout, this);

        setBackgroundColor(Color.WHITE);
        setOrientation(VERTICAL);

        this.titleTextView = findViewById(R.id.title_text_view);
        this.closeButton = findViewById(R.id.close_button);

        this.closeButton.setOnClickListener(view -> pickerListener.onPickerClosed());

        if (picker == Picker.Emoji) {
            titleTextView.setText("Emoji");

            EmojiPicker emojiPicker = new EmojiPicker(this.getContext(), this.pickerListener);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            emojiPicker.setLayoutParams(layoutParams);
            addView(emojiPicker);
        } else if (picker == Picker.Background) {
            throw new RuntimeException();
        }
    }
}