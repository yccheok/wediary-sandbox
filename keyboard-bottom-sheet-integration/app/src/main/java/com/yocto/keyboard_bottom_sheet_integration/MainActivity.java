package com.yocto.keyboard_bottom_sheet_integration;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsAnimationCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PickerListener {

    private int systemBarsHeight = 0;
    private int keyboardHeightWhenVisible = 0;
    private boolean keyboardVisible = false;

    private FrameLayout keyboardView;

    private Picker pickerAfterKeyboardHeight = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keyboardView = findViewById(R.id.keyboard_view);

        findViewById(R.id.image_button_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundPicker();
            }
        });

        findViewById(R.id.image_button_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emojiPicker();
            }
        });

        final View rootView = getWindow().getDecorView().getRootView();

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            boolean imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime());

            systemBarsHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;

            keyboardVisible = imeVisible;

            Log.i("CHEOK", "init keyboardVisible = " + keyboardVisible);

            if (keyboardVisible) {
                keyboardHeightWhenVisible = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;

                if (keyboardView.getChildCount() > 0) {
                    slideDownThenRemoveAllViews();
                }

                handlePickerAfterKeyboardHeight();
            }

            // https://stackoverflow.com/questions/75325095/how-to-use-windowinsetscompat-correctly-to-listen-to-keyboard-height-change-in-a
            return ViewCompat.onApplyWindowInsets(v, insets);

        });

        WindowInsetsAnimationCompat.Callback callback = new WindowInsetsAnimationCompat.Callback(
                WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_STOP
        ) {
            @Override
            public void onPrepare(@NonNull WindowInsetsAnimationCompat animation) {
                super.onPrepare(animation);
                // disable adjustPan.
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            }

            @Override
            public void onEnd(@NonNull WindowInsetsAnimationCompat animation) {
                super.onEnd(animation);
                // Enable adjustPan.
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            }

            @NonNull
            @Override
            public WindowInsetsCompat onProgress(@NonNull WindowInsetsCompat insets, @NonNull List<WindowInsetsAnimationCompat> runningAnimations) {
                if (keyboardVisible) {
                    if (keyboardView.getHeight() == getGoodKeyboardViewHeight()) {
                        return insets;
                    }
                } else {
                    if (keyboardView.getChildCount() > 0) {
                        return insets;
                    }
                }

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
                }
                return insets;

            }
        };

        ViewCompat.setWindowInsetsAnimationCallback(rootView, callback);

    }

    private void slideDownThenRemoveAllViews() {
        View view = keyboardView.getChildAt(0);

        Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                keyboardView.removeAllViews();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(slideDown);
    }

    private void showKeyboard() {
        final View rootView = getWindow().getDecorView().getRootView();

        ViewCompat.getWindowInsetsController(rootView).show(WindowInsetsCompat.Type.ime());
    }

    private void hideKeyboard() {
        final View rootView = getWindow().getDecorView().getRootView();

        ViewCompat.getWindowInsetsController(rootView).hide(WindowInsetsCompat.Type.ime());
    }

    private void handlePickerAfterKeyboardHeight() {
        if (pickerAfterKeyboardHeight == Picker.Emoji) {
            emojiPicker();
        } else if (pickerAfterKeyboardHeight == Picker.Background) {
            backgroundPicker();
        }

        pickerAfterKeyboardHeight = null;
    }

    private boolean handleCaseWhenKeyboardHeightIsNotReady(Picker picker) {
        if (keyboardHeightWhenVisible <= 0) {
            this.pickerAfterKeyboardHeight = picker;

            showKeyboard();

            return true;
        }

        return false;
    }

    private int getGoodKeyboardViewHeight() {
        return (keyboardHeightWhenVisible - systemBarsHeight);
    }

    private void backgroundPicker() {
        if (handleCaseWhenKeyboardHeightIsNotReady(Picker.Background)) {
            return;
        }

        ViewGroup.LayoutParams params = keyboardView.getLayoutParams();
        params.height = getGoodKeyboardViewHeight();
        keyboardView.setLayoutParams(params);

        BackgroundPicker backgroundPicker = new BackgroundPicker(this, this);
        keyboardView.removeAllViews();
        keyboardView.addView(backgroundPicker);

        hideKeyboard();
    }

    private void emojiPicker() {
        if (handleCaseWhenKeyboardHeightIsNotReady(Picker.Emoji)) {
            return;
        }

        ViewGroup.LayoutParams params = keyboardView.getLayoutParams();
        params.height = getGoodKeyboardViewHeight();
        keyboardView.setLayoutParams(params);

        EmojiPicker emojiPicker = new EmojiPicker(this, this);
        keyboardView.removeAllViews();
        keyboardView.addView(emojiPicker);

        hideKeyboard();
    }

    @Override
    public void onBackPressed() {
        if (!keyboardVisible) {
            if (keyboardView.getChildCount() > 0) {
                shrinkHeightThenRemoveAllViews();
                return;
            }
        }

        super.onBackPressed();
    }

    private void shrinkHeightThenRemoveAllViews() {
        ValueAnimator shrinkAnimator = ValueAnimator
                .ofInt(keyboardView.getHeight(), 0)
                .setDuration(150);

        shrinkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                Integer value = (Integer) valueAnimator.getAnimatedValue();
                keyboardView.getLayoutParams().height = value.intValue();
                keyboardView.requestLayout();
            }
        });


        AnimatorSet animationSet = new AnimatorSet();
        animationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                keyboardView.removeAllViews();
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {
                keyboardView.removeAllViews();
            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }
        });

        // animationSet.setInterpolator(new AccelerateDecelerateInterpolator());

        animationSet.play(shrinkAnimator);
        animationSet.start();
    }

    @Override
    public void onPickerClosed() {
        showKeyboard();
    }
}