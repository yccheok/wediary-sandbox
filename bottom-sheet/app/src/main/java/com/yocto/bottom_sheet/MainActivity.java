package com.yocto.bottom_sheet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class MainActivity extends AppCompatActivity {

    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.image_button_0).setOnClickListener(view -> demo0());

        findViewById(R.id.image_button_1).setOnClickListener(view -> demo1());

        // 7) A non-blocking bottom sheet. When we tap on non-bottom sheet item, the tapped item
        // will get focus and bottom sheet will hide.
        findViewById(R.id.edit_text_0).setOnFocusChangeListener((view, b) -> {
            if (b) {
                hideBottomSheet();
            }
        });

        // 7) A non-blocking bottom sheet. When we tap on non-bottom sheet item, the tapped item
        // will get focus and bottom sheet will hide.
        findViewById(R.id.edit_text_1).setOnFocusChangeListener((view, b) -> {
            if (b) {
                hideBottomSheet();
            }
        });
    }

    public void demo0() {
        DemoBottomDialogFragment demoBottomDialogFragment = DemoBottomDialogFragment.newInstance();

        demoBottomDialogFragment.show(getSupportFragmentManager(), "demoBottomDialogFragment");
    }

    public void demo1() {
        // 1) Round corner bottom sheet.
        View view = findViewById(R.id.bottom_sheet_layout_2);

        /*
        2) Fixed height bottom sheet.

        3) Non-draggable bottom sheet.

        4) Content in the bottom sheet is scrollable.
         */
        this.bottomSheetBehavior = BottomSheetBehavior.from(view);

        bottomSheetBehavior.setPeekHeight(900, true);

        bottomSheetBehavior.setDraggable(false);
    }

    private boolean hideBottomSheet() {
        if (this.bottomSheetBehavior != null) {
            this.bottomSheetBehavior.setPeekHeight(0, true);
            this.bottomSheetBehavior = null;
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        // 5) Hide bottom sheet when we tap on non-bottom sheet item.
        if (hideBottomSheet()) {
            return;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 6) Hide sheet when we press on back button.
        hideBottomSheet();

        return super.onTouchEvent(event);
    }
}