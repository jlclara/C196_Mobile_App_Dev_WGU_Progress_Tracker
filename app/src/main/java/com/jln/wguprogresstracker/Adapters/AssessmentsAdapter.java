package com.jln.wguprogresstracker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jln.wguprogresstracker.Activity.AddAssessment;
import com.jln.wguprogresstracker.DatabaseHelper;
import com.jln.wguprogresstracker.R;

public class AssessmentsAdapter extends RecyclerView.Adapter<AssessmentsAdapter.AssessmentsViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public AssessmentsAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor=cursor;

    }
    public class AssessmentsViewHolder extends RecyclerView.ViewHolder {

        public TextView displayText;


        public AssessmentsViewHolder(View itemView) {
            super(itemView);
            displayText = itemView.findViewById(R.id.textView_assessmentName);

        }
    }

    @Override
    public AssessmentsAdapter.AssessmentsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.assessments_item, viewGroup, false);
        return new AssessmentsAdapter.AssessmentsViewHolder(view);
    }

    public void onBindViewHolder(AssessmentsAdapter.AssessmentsViewHolder viewHolder, int i) {
        if (!mCursor.moveToPosition(i)) {
            return;
        }
        final Integer id = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.ASSESSMENT_COL_1));
        final String type = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.ASSESSMENT_COL_2));
        final String title = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.ASSESSMENT_COL_3));
        final String dueDate = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.ASSESSMENT_COL_4));
        final String goalDate = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.ASSESSMENT_COL_5));
        final Integer goalDateAlert = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.ASSESSMENT_COL_6));
        final String courseFID = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.ASSESSMENT_COL_7));
        final Integer alertCode = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.ASSESSMENT_COL_8));

        viewHolder.displayText.setText(title + ": " + type);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddAssessment.class);
                intent.putExtra("ASSESSMENT_COL_1", id);
                intent.putExtra("ASSESSMENT_COL_2", type);
                intent.putExtra("ASSESSMENT_COL_3", title);
                intent.putExtra("ASSESSMENT_COL_4", dueDate);
                intent.putExtra("ASSESSMENT_COL_5", goalDate);
                intent.putExtra("ASSESSMENT_COL_6", goalDateAlert);
                intent.putExtra("ASSESSMENT_COL_7", courseFID);
                intent.putExtra("ASSESSMENT_COL_8", alertCode);


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
