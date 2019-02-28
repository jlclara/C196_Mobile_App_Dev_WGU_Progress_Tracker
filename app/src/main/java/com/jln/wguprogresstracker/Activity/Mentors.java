package com.jln.wguprogresstracker.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jln.wguprogresstracker.Adapters.MentorsAdapter;
import com.jln.wguprogresstracker.DatabaseHelper;
import com.jln.wguprogresstracker.R;

public class Mentors extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private MentorsAdapter mAdapter;
    private static boolean loaded = false;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentors);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

        mDatabase = dbHelper.getWritableDatabase();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MentorsAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));


        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent termIntent = new Intent(getBaseContext(), AddMentorActivity.class);
                startActivity(termIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent termIntent = new Intent(getBaseContext(), AddMentorActivity.class);
        startActivity(termIntent);
        mAdapter.swapCursor(getAllItems());


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));

    }

    private Cursor getAllItems() {
        return mDatabase.query(
                DatabaseHelper.MENTOR_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!loaded) {
            //First time just set the loaded flag true
            loaded = true;
        } else {

            mAdapter = new MentorsAdapter(this, getAllItems());
            recyclerView.setAdapter(mAdapter);
        }
    }


}
