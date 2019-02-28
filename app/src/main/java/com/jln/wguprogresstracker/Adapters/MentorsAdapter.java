package com.jln.wguprogresstracker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jln.wguprogresstracker.Activity.AddMentorActivity;
import com.jln.wguprogresstracker.DatabaseHelper;
import com.jln.wguprogresstracker.R;

public class MentorsAdapter extends RecyclerView.Adapter<MentorsAdapter.MentorsViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public MentorsAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor=cursor;

    }
    public class MentorsViewHolder extends RecyclerView.ViewHolder {

        public TextView nameText;
        public TextView startText;
        public TextView endText;

        public MentorsViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textView_name);

        }
    }

    @Override
    public MentorsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.mentors_item, viewGroup, false);
        return new MentorsViewHolder(view);
    }

    public void onBindViewHolder(MentorsViewHolder viewHolder, int i) {
        if (!mCursor.moveToPosition(i)) {
            return;
        }
        final Integer id = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.MENTOR_COL_1));
        final String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.MENTOR_COL_2));
        final String phone = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.MENTOR_COL_3));
        final String emailAddress = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.MENTOR_COL_4));

        viewHolder.nameText.setText(name);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddMentorActivity.class);
                intent.putExtra("MENTOR_COL_1", id);
                intent.putExtra("MENTOR_COL_2", name);
                intent.putExtra("MENTOR_COL_3", phone);
                intent.putExtra("MENTOR_COL_4", emailAddress);

                mContext.startActivity(intent);
            }

            ;
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
