package com.yocto.keyboard_bottom_sheet_integration;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.yocto.keyboard_bottom_sheet_integration.model.Emoji;

public class EmojiPopup {
    final View targetView;
    final Emoji emoji;

    PopupWindow popupWindow;
    View view;

    public EmojiPopup(View targetView, Emoji emoji) {
        this.targetView = targetView;
        this.emoji = emoji;
    }

    public void show() {
        Log.i("CHEOK", "show!");

        Context context = targetView.getContext();

        this.view = View.inflate(context, R.layout.emoji_popup, null);

        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // So that the popup window will dismiss if we touch anywhere.
        popupWindow.setOutsideTouchable(true);

        popupWindow.setOnDismissListener(() -> Log.i("CHEOK", "Pop up window is dismissed"));

        final int[] location = new int[2];
        targetView.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        Log.i("CHEOK", x + ", " + y);
        //popupWindow.showAtLocation(targetView, Gravity.NO_GRAVITY, x, y);
        popupWindow.showAsDropDown(targetView, 0, -280);
    }
}