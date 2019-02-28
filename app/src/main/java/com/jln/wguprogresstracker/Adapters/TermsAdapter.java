package com.jln.wguprogresstracker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jln.wguprogresstracker.Activity.AddTermActivity;
import com.jln.wguprogresstracker.DatabaseHelper;
import com.jln.wguprogresstracker.R;

public class TermsAdapter extends RecyclerView.Adapter<TermsAdapter.TermsViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public TermsAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor=cursor;

    }
    public class TermsViewHolder extends RecyclerView.ViewHolder {

        public TextView nameText;
        public TextView startText;
        public TextView endText;

        public TermsViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textView_name);
//            startText = itemView.findViewById(R.id.textView_start);
//            endText = itemView.findViewById(R.id.textView_end);
        }
    }

    @Override
    public TermsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.terms_item, viewGroup, false);
        return new TermsViewHolder(view);
    }

    public void onBindViewHolder(TermsViewHolder viewHolder, int i) {
        if (!mCursor.moveToPosition(i)) {
            return;
        }
        final Integer id = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.TERM_COL_1));
        final String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TERM_COL_2));
        final String start = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TERM_COL_3));
        final String end = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TERM_COL_4));

        viewHolder.nameText.setText(name + ": " + start + " - " + end);
      //  viewHolder.startText.setText(start);
      //  viewHolder.endText.setText(end);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddTermActivity.class);
                intent.putExtra("TERM_COL_1", id);
                intent.putExtra("TERM_COL_2", name);
                intent.putExtra("TERM_COL_3", start);
                intent.putExtra("TERM_COL_4", end);

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
