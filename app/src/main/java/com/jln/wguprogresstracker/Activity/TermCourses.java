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

import com.jln.wguprogresstracker.Adapters.CoursesAdapter;
import com.jln.wguprogresstracker.DatabaseHelper;
import com.jln.wguprogresstracker.R;

public class TermCourses extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private CoursesAdapter mAdapter;
    private static boolean loaded = false;
    private String termName;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

        mDatabase = dbHelper.getWritableDatabase();

        getIncomingIntent();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CoursesAdapter(this, getSelectedCourses(termName));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent courseIntent = new Intent(getBaseContext(), AddCourse.class);
                startActivity(courseIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent courseIntent = new Intent(getBaseContext(), AddCourse.class);
        courseIntent.putExtra("TERM_COL_2", termName);
        startActivity(courseIntent);
        mAdapter.swapCursor(getSelectedCourses(termName));
        return super.onOptionsItemSelected(item);

    }

    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("TERM_COL_2"))
        {
            termName = getIntent().getStringExtra("TERM_COL_2");
            Toolbar mActionBarToolbar = (Toolbar)findViewById(R.id.toolbar);
            mActionBarToolbar.setTitle(termName + " Courses");

            return true;
        }
        else {

            return false;
        }
    }

    private Cursor getSelectedCourses(String termName) {
        return mDatabase.query(
                DatabaseHelper.COURSE_TABLE_NAME,
                null,
                "COURSE_TERM_FID = '" + termName + "'",
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

            mAdapter = new CoursesAdapter(this, getSelectedCourses(termName));
            recyclerView.setAdapter(mAdapter);
        }
    }
//    @Override
//    public void onBackPressed() {
//        finish();
//    }


}
