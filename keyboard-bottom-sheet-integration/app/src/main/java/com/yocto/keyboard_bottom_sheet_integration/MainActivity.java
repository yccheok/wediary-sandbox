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
import android.widget.LinearLayout;

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

    private FrameLayout pickerFrameLayout;
    private LinearLayout bottomLinearLayout;

    private Picker pickerAfterKeyboardHeight = null;

    private static void setMargins(View v, int l, int t, int r, int b) {
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        p.setMargins(l, t, r, b);
        v.requestLayout();
    }

    private boolean hasPickerLayout() {
        return pickerFrameLayout.getChildCount() > 1;
    }

    private View getPickerLayout() {
        return pickerFrameLayout.getChildAt(1);
    }

    private void addPickerLayout(PickerLayout pickerLayout) {
        pickerFrameLayout.addView(pickerLayout);
    }

    private void removeAllPickerLayouts() {
        while (pickerFrameLayout.getChildCount() > 1) {
            pickerFrameLayout.removeViewAt(pickerFrameLayout.getChildCount()-1);
        }
    }

    private int getBottomLinearLayoutBottomMargin() {
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) bottomLinearLayout.getLayoutParams();
        return p.bottomMargin;
    }

    private void setBottomLinearLayoutBottomMargin(int bottomMargin) {
        setMargins(bottomLinearLayout, 0, 0, 0, bottomMargin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickerFrameLayout = findViewById(R.id.picker_frame_layout);
        bottomLinearLayout = findViewById(R.id.bottom_linear_layout);

        bottomLinearLayout.
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

            if (keyboardVisible) {
                keyboardHeightWhenVisible = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;

                if (hasPickerLayout()) {
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
                    if (getBottomLinearLayoutBottomMargin() == getGoodKeyboardViewHeight()) {
                        return insets;
                    }
                } else {
                    if (hasPickerLayout()) {
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

                    setBottomLinearLayoutBottomMargin(keyboardViewHeight);
                }
                return insets;

            }
        };

        ViewCompat.setWindowInsetsAnimationCallback(rootView, callback);

    }

    private void slideDownThenRemoveAllViews() {
        bottomLinearLayout.setVisibility(View.VISIBLE);
        
        final View view = getPickerLayout();

        Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                removeAllPickerLayouts();
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

        hideKeyboard();
    }

    private void emojiPicker() {
        if (handleCaseWhenKeyboardHeightIsNotReady(Picker.Emoji)) {
            return;
        }

        setBottomLinearLayoutBottomMargin(getGoodKeyboardViewHeight());

        PickerLayout pickerLayout = new PickerLayout(this, Picker.Emoji, this);
        removeAllPickerLayouts();
        addPickerLayout(pickerLayout);
        bottomLinearLayout.setVisibility(View.INVISIBLE);

        hideKeyboard();
    }

    @Override
    public void onBackPressed() {
        if (!keyboardVisible) {
            if (hasPickerLayout()) {
                shrinkHeightThenRemoveAllViews();
                return;
            }
        }

        super.onBackPressed();
    }

    private void shrinkHeightThenRemoveAllViews() {
        ValueAnimator shrinkAnimator = ValueAnimator
                .ofInt(getBottomLinearLayoutBottomMargin(), 0)
                .setDuration(150);

        shrinkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                Integer value = (Integer) valueAnimator.getAnimatedValue();
                setBottomLinearLayoutBottomMargin(value);
            }
        });


        AnimatorSet animationSet = new AnimatorSet();
        animationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                removeAllPickerLayouts();
                bottomLinearLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {
                removeAllPickerLayouts();
                bottomLinearLayout.setVisibility(View.VISIBLE);
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