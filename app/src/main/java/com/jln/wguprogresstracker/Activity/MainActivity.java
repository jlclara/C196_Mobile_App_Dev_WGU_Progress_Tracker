package com.jln.wguprogresstracker.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.jln.wguprogresstracker.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onTermBtnClicked(View v){
        Intent termIntent = new Intent(getBaseContext(), Terms.class);
        startActivity(termIntent);
    }

    public void onMentorBtnClicked(View v){
        Intent termIntent = new Intent(getBaseContext(), Mentors.class);
        startActivity(termIntent);
    }

    public void onCourseBtnClicked(View v){
        Intent termIntent = new Intent(getBaseContext(), Courses.class);
        startActivity(termIntent);
    }

    public void onAssessmentBtnClicked(View v){
        Intent termIntent = new Intent(getBaseContext(), Assessments.class);
        startActivity(termIntent);
    }
}
