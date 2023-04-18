package com.yocto.keyboard_bottom_sheet_integration;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.cardview.widget.CardView;
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
        this.view.setVisibility(View.INVISIBLE);

        this.recyclerView = this.view.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 6);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(emojiPopupAdapter);

        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // So that the popup window will dismiss if we touch anywhere.
        popupWindow.setOutsideTouchable(true);

        int targetViewHeight = targetView.getHeight();
        popupWindow.showAsDropDown(targetView, 0, 0);

        final int[] locations = new int[2];
        targetView.getLocationOnScreen(locations);
        final int targetViewX = locations[0];
        final int targetViewWidth = targetView.getWidth();

        Utils.onGlobalLayout(view, () -> {
            popupWindow.getContentView(). getLocationOnScreen(locations);
            int popupWindowX = locations[0];

            // We can now obtain the height of view. Now, we need to dismiss it first, before
            // showing the popupWindow in correct position again.
            popupWindow.dismiss();

            popupWindow.setOnDismissListener(() -> Log.i("CHEOK", "Pop up window is dismissed"));

            final int yOffset = -targetViewHeight-view.getHeight();

            popupWindow.showAsDropDown(targetView, 0, yOffset);

            ImageView arrowImageView = view.findViewById(R.id.arrow_image_view);
            CardView cardView = view.findViewById(R.id.cardview);

            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)arrowImageView.getLayoutParams();
            ViewGroup.MarginLayoutParams cardViewMarginLayoutParams = (ViewGroup.MarginLayoutParams)cardView.getLayoutParams();
            final int cardViewMarginStart = cardViewMarginLayoutParams.getMarginStart();

            // Not quite sure why two cardViewMarginStart is required.
            marginLayoutParams.leftMargin =
                    (targetViewWidth) / 2 +
                    -cardViewMarginStart -cardViewMarginStart +
                    (targetViewX - popupWindowX);

            arrowImageView.setLayoutParams(marginLayoutParams);

            view.setVisibility(View.VISIBLE);
        });
    }
}