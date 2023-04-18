package com.yocto.keyboard_bottom_sheet_integration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yocto.keyboard_bottom_sheet_integration.model.Emoji;

import java.util.ArrayList;
import java.util.List;

public class EmojiPopupAdapter extends RecyclerView.Adapter<EmojiPopupAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view);
        }
    }

    private final Context context;
    private final List<Emoji> emojis = new ArrayList<>();

    public EmojiPopupAdapter(Context context, List<Emoji> emojis) {
        this.context = context;

        this.emojis.addAll(emojis);
    }

    @NonNull
    @Override
    public EmojiPopupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emoji_popup_adapter, parent, false);
/*
        final ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        int col = Utils.getScreenWidth() / Utils.dpToPixel(48);
        int widthPerCol = Utils.getScreenWidth() / col;
        layoutParams.height = widthPerCol;
        itemView.setLayoutParams(layoutParams);*/

        EmojiPopupAdapter.ViewHolder viewHolder = new EmojiPopupAdapter.ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiPopupAdapter.ViewHolder holder, int position) {
        Emoji emoji = emojis.get(position);

        holder.textView.setText(emoji.unicode);
    }

    @Override
    public int getItemCount() {
        return this.emojis.size();
    }
}
