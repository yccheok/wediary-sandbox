package com.yocto.keyboard_bottom_sheet_integration;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yocto.keyboard_bottom_sheet_integration.model.Emoji;
import com.yocto.keyboard_bottom_sheet_integration.repository.EmojiRoomDatabase;

import java.util.List;

public class EmojiPopup {
    final View targetView;
    final Emoji emoji;
    final EmojiPopupAdapter emojiPopupAdapter;

    PopupWindow popupWindow;
    View view;
    RecyclerView recyclerView;

    public EmojiPopup(View targetView, Emoji emoji) {
        this.targetView = targetView;
        this.emoji = emoji;

        Context context = targetView.getContext();
        List<Emoji> emojis = EmojiRoomDatabase.instance(context).emojiDao().getEmojis(
                emoji.group
        );
        this.emojiPopupAdapter = new EmojiPopupAdapter(context, emojis);
    }

    public void show() {
        Context context = targetView.getContext();

        this.view = View.inflate(context, R.layout.emoji_popup, null);

        this.recyclerView = this.view.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 6);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(emojiPopupAdapter);

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
        popupWindow.showAsDropDown(targetView, 0, -350);
    }
}