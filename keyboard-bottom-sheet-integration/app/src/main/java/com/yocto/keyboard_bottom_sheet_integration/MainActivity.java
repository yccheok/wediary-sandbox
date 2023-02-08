package com.yocto.keyboard_bottom_sheet_integration;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsAnimationCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int systemBarsHeight = 0;
    private int keyboardHeightWhenVisible = 0;
    private boolean keyboardVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View keyboardView = findViewById(R.id.keyboard_view);

        final View rootView = getWindow().getDecorView().getRootView();

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            boolean imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime());

            systemBarsHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;

            keyboardVisible = imeVisible;
            if (keyboardVisible) {
                keyboardHeightWhenVisible = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
            }

            // https://stackoverflow.com/questions/75325095/how-to-use-windowinsetscompat-correctly-to-listen-to-keyboard-height-change-in-a
            return ViewCompat.onApplyWindowInsets(v, insets);

        });

        WindowInsetsAnimationCompat.Callback callback = new WindowInsetsAnimationCompat.Callback(
                WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_STOP
        ) {
            @NonNull
            @Override
            public WindowInsetsCompat onProgress(@NonNull WindowInsetsCompat insets, @NonNull List<WindowInsetsAnimationCompat> runningAnimations) {
                // Find an IME animation.
                WindowInsetsAnimationCompat imeAnimation = null;
                for (WindowInsetsAnimationCompat animation : runningAnimations) {
                    if ((animation.getTypeMask() & WindowInsetsCompat.Type.ime()) != 0) {
                        imeAnimation = animation;
                        break;
                    }
                }
                if (imeAnimation != null) {
                    int keyboardViewHeight;
                    if (keyboardVisible) {
                        keyboardViewHeight = (int) (keyboardHeightWhenVisible * imeAnimation.getInterpolatedFraction()) - systemBarsHeight;
                    } else {
                        keyboardViewHeight = (int) (keyboardHeightWhenVisible * (1.0-imeAnimation.getInterpolatedFraction())) - systemBarsHeight;
                    }

                    keyboardViewHeight = Math.max(0, keyboardViewHeight);
                    
                    ViewGroup.LayoutParams params = keyboardView.getLayoutParams();
                    params.height = keyboardViewHeight;
                    keyboardView.setLayoutParams(params);

                    Log.i("CHEOK", "keyboardViewHeight = " + keyboardViewHeight);
                }
                return insets;

            }
        };

        ViewCompat.setWindowInsetsAnimationCallback(rootView, callback);

    }
}