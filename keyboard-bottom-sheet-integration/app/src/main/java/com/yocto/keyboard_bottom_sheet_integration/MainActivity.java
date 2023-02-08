package com.yocto.keyboard_bottom_sheet_integration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View keyboardView = findViewById(R.id.keyboard_view);

        View rootView = getWindow().getDecorView().getRootView();

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            boolean imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime());

            // https://stackoverflow.com/questions/75325095/how-to-use-windowinsetscompat-correctly-to-listen-to-keyboard-height-change-in-a
            int imeHeight =
                    insets.getInsets(WindowInsetsCompat.Type.ime()).bottom -
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;

            android.util.Log.i("CHEOK", "imeVisible = " + imeVisible + ", imeHeight = " + imeHeight);

            ViewGroup.LayoutParams params = keyboardView.getLayoutParams();
            params.height = imeHeight;
            keyboardView.setLayoutParams(params);

            // https://stackoverflow.com/questions/75325095/how-to-use-windowinsetscompat-correctly-to-listen-to-keyboard-height-change-in-a
            return ViewCompat.onApplyWindowInsets(v, insets);
        });
    }
}