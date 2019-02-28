package com.jln.wguprogresstracker.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jln.wguprogresstracker.Adapters.NotesAdapter;
import com.jln.wguprogresstracker.DatabaseHelper;
import com.jln.wguprogresstracker.R;

public class CourseNotes extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private NotesAdapter mAdapter;
    private static boolean loaded = false;
    private RecyclerView recyclerView;
    private String courseText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

        mDatabase = dbHelper.getWritableDatabase();
        getIncomingIntent();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NotesAdapter(this, getCourseNotes(courseText));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent courseIntent = new Intent(getBaseContext(), AddNoteActivity.class);
                startActivity(courseIntent);
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

        Intent courseIntent = new Intent(getBaseContext(), AddNoteActivity.class);
        courseIntent.putExtra("COURSE_COL_2", courseText);
        startActivity(courseIntent);
        mAdapter.swapCursor(getCourseNotes(courseText));
        return super.onOptionsItemSelected(item);
    }


    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("COURSE_COL_2"))
        {
            courseText = getIntent().getStringExtra("COURSE_COL_2");
            Toolbar mActionBarToolbar = (Toolbar)findViewById(R.id.toolbar);
            mActionBarToolbar.setTitle(courseText + " Course Notes");
            return true;
        }
        else {

            return false;
        }
    }

    private Cursor getCourseNotes(String courseName) {
        return mDatabase.query(
                DatabaseHelper.NOTE_TABLE_NAME,
                null,
                "NOTE_COURSE_ID = '" + courseName + "'",
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

            mAdapter = new NotesAdapter(this, getCourseNotes(courseText));
            recyclerView.setAdapter(mAdapter);
        }
    }


}
