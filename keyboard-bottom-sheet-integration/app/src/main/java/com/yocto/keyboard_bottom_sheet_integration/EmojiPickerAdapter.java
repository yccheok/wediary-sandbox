package com.yocto.keyboard_bottom_sheet_integration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yocto.keyboard_bottom_sheet_integration.model.Emoji;
import com.yocto.keyboard_bottom_sheet_integration.repository.EmojiDao;
import com.yocto.keyboard_bottom_sheet_integration.repository.EmojiRoomDatabase;

import java.util.ArrayList;
import java.util.List;

public class EmojiPickerAdapter extends RecyclerView.Adapter<EmojiPickerAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.recycler_view);

            int col = Utils.getScreenWidth() / Utils.dpToPixel(48);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(itemView.getContext(), col);

            recyclerView.setLayoutManager(gridLayoutManager);
        }
    }

    private final Context context;
    private final List<List<Emoji>> emojisDictionary = new ArrayList<>();

    public EmojiPickerAdapter(Context context) {
        this.context = context;

        EmojiDao emojiDao = EmojiRoomDatabase.instance(context).emojiDao();

        // Recent.
        emojisDictionary.add(new ArrayList<>());
        for (int category=1; category<=8; category++) {
            List<Emoji> emojis = emojiDao.getEmojis(category);
            emojisDictionary.add(emojis);
        }
    }

    @NonNull
    @Override
    public EmojiPickerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emoji_picker_adapter, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<Emoji> emojis = emojisDictionary.get(position);

        EmojiAdapter emojiAdapter = new EmojiAdapter(context, emojis);

        holder.recyclerView.setAdapter(emojiAdapter);
    }

    @Override
    public int getItemCount() {
        return EmojiCategory.values().length;
    }
}