package com.lovelycoding.magicnote.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lovelycoding.magicnote.R;
import com.lovelycoding.magicnote.models.Note;
import com.lovelycoding.magicnote.util.Utility;

import java.util.ArrayList;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {
    private static final String TAG = "NotesRecyclerAdapter";
    private ArrayList<Note> mNotes = new ArrayList<>();
    private OnNoteListener mNoteListener;

    public NotesRecyclerAdapter(ArrayList<Note> notes, OnNoteListener listener) {
        this.mNotes = notes;
        this.mNoteListener=listener;
        Log.i(TAG, "constructor");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_note_list_item, parent, false);

        Log.i(TAG, "onCreateViewHolder ");
        return new ViewHolder(view,mNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        
        try {
                String month=mNotes.get(position).getTimestamp().substring(0,2);
                month= Utility.getMonthFormNumber(month);
                String year=mNotes.get(position).getTimestamp().substring(3);
                String timestamp=month+" "+year;
            holder.tvTtile.setText(mNotes.get(position).getTitle());
            holder.tvTimestamp.setText(timestamp);


        }catch (NullPointerException e){
            Log.d(TAG, "onBindViewHolder: NullPointerException "+e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTtile, tvTimestamp;
        OnNoteListener mNoteListener;

        public ViewHolder(@NonNull View itemView,OnNoteListener listener) {
            super(itemView);
            this.mNoteListener=listener;
            tvTtile = itemView.findViewById(R.id.tv_note_title);
            tvTimestamp = itemView.findViewById(R.id.tv_note_timestamp);
            Log.i(TAG, "ViewHolder ");
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        public void onNoteClick(int position);
    }
}
