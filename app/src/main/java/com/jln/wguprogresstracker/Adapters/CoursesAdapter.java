package com.jln.wguprogresstracker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jln.wguprogresstracker.Activity.AddCourse;
import com.jln.wguprogresstracker.DatabaseHelper;
import com.jln.wguprogresstracker.R;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CoursesViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public CoursesAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor=cursor;

    }
    public class CoursesViewHolder extends RecyclerView.ViewHolder {

        public TextView displayText;


        public CoursesViewHolder(View itemView) {
            super(itemView);
            displayText = itemView.findViewById(R.id.textView_courseName);

        }
    }

    @Override
    public CoursesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.courses_items, viewGroup, false);
        return new CoursesViewHolder(view);
    }

    public void onBindViewHolder(CoursesViewHolder viewHolder, int i) {
        if (!mCursor.moveToPosition(i)) {
            return;
        }
        final Integer id = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.COURSE_COL_1));
        final String title = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COURSE_COL_2));
        final String status = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COURSE_COL_3));
        final String startDate = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COURSE_COL_4));
        final Integer startAlert = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.COURSE_COL_5));
        final String endDate = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COURSE_COL_6));
        final Integer endAlert = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.COURSE_COL_7));
        final String notes = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COURSE_COL_8));
        final String mentor = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COURSE_COL_9));
        final String assessment = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COURSE_COL_10));
        final String term = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COURSE_COL_11));
        final Integer alertCode = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.COURSE_COL_12));


        viewHolder.displayText.setText(title + ": " + status);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddCourse.class);
                intent.putExtra("COURSE_COL_1", id);
                intent.putExtra("COURSE_COL_2", title);
                intent.putExtra("COURSE_COL_3", status);
                intent.putExtra("COURSE_COL_4", startDate);
                intent.putExtra("COURSE_COL_5", startAlert);
                intent.putExtra("COURSE_COL_6", endDate);
                intent.putExtra("COURSE_COL_7", endAlert);
                intent.putExtra("COURSE_COL_8", notes);
                intent.putExtra("COURSE_COL_9", mentor);
                intent.putExtra("COURSE_COL_10", assessment);
                intent.putExtra("COURSE_COL_11", term);
                intent.putExtra("COURSE_COL_12", alertCode);


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