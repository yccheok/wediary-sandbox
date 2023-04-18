package com.yocto.keyboard_bottom_sheet_integration;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yocto.keyboard_bottom_sheet_integration.model.Emoji;

import java.util.ArrayList;
import java.util.List;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view);
        }
    }

    private final Context context;
    private final List<Emoji> emojis = new ArrayList<>();

    public EmojiAdapter(Context context, List<Emoji> emojis) {
        this.context = context;

        this.emojis.addAll(emojis);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emoji_adapter, parent, false);

        // Make sure it is "square" size.
        final ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        int col = Utils.getScreenWidth() / Utils.dpToPixel(48);
        int widthPerCol = Utils.getScreenWidth() / col;
        layoutParams.height = widthPerCol;
        itemView.setLayoutParams(layoutParams);

        ViewHolder viewHolder = new ViewHolder(itemView);

        itemView.setOnClickListener(view -> {
            final int adapterPosition = viewHolder.getAdapterPosition();
            Emoji emoji = emojis.get(adapterPosition);
            Log.i("CHEOK", "id = " + emoji.id);
        });

        itemView.setOnLongClickListener(view -> {
            final int adapterPosition = viewHolder.getAdapterPosition();

            Emoji emoji = emojis.get(adapterPosition);

            if (emoji.group != null) {
                EmojiPopup emojiPopup = new EmojiPopup(view, emoji);

                emojiPopup.show();
            }

            return true;
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Emoji emoji = emojis.get(position);

        holder.textView.setText(emoji.unicode);
    }

    @Override
    public int getItemCount() {
        return this.emojis.size();
    }
}
