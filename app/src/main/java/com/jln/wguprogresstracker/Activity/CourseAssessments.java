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

import com.jln.wguprogresstracker.Adapters.AssessmentsAdapter;
import com.jln.wguprogresstracker.DatabaseHelper;
import com.jln.wguprogresstracker.R;

public class CourseAssessments extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private AssessmentsAdapter mAdapter;
    private String courseText;
    private static boolean loaded = false;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

        mDatabase = dbHelper.getWritableDatabase();

        getIncomingIntent();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AssessmentsAdapter(this, getSelectedAssessments(courseText));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent assessmentIntent = new Intent(getBaseContext(), AddAssessment.class);
                startActivity(assessmentIntent);
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

        Intent assessmentIntent = new Intent(getBaseContext(), AddAssessment.class);
        assessmentIntent.putExtra("COURSE_COL_2", courseText);
        startActivity(assessmentIntent);
        mAdapter.swapCursor(getSelectedAssessments(courseText));
        return super.onOptionsItemSelected(item);
    }


    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("COURSE_COL_2"))
        {
            courseText = getIntent().getStringExtra("COURSE_COL_2");
            Toolbar mActionBarToolbar = (Toolbar)findViewById(R.id.toolbar);
            mActionBarToolbar.setTitle(courseText + " Assessments");
            return true;
        }
        else
            return false;
    }


    public Cursor getSelectedAssessments(String courseName) {
        return mDatabase.query(
                DatabaseHelper.ASSESSMENT_TABLE_NAME,
                null,
                "COURSE_FID = '" + courseName +"'",
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

            mAdapter = new AssessmentsAdapter(this, getSelectedAssessments(courseText));
            recyclerView.setAdapter(mAdapter);
        }
    }

}
