package com.jln.wguprogresstracker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jln.wguprogresstracker.Activity.AddNoteActivity;
import com.jln.wguprogresstracker.DatabaseHelper;
import com.jln.wguprogresstracker.R;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public NotesAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor=cursor;

    }
    public class NotesViewHolder extends RecyclerView.ViewHolder {

        public TextView displayText;


        public NotesViewHolder(View itemView) {
            super(itemView);
            displayText = itemView.findViewById(R.id.textViewNotesItem);
        }
    }

    @Override
    public NotesAdapter.NotesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.note_items, viewGroup, false);
        return new NotesAdapter.NotesViewHolder(view);
    }

    public void onBindViewHolder(NotesAdapter.NotesViewHolder viewHolder, int i) {
        if (!mCursor.moveToPosition(i)) {
            return;
        }
        final Integer id = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.NOTE_COL_1));
        final String courseId = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.NOTE_COL_2));
        final String message = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.NOTE_COL_3));


        viewHolder.displayText.setText(message);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddNoteActivity.class);
                intent.putExtra("NOTE_COL_1", id);
                intent.putExtra("NOTE_COL_2", courseId);
                intent.putExtra("NOTE_COL_3", message);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor  = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

}
