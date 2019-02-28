package com.jln.wguprogresstracker.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jln.wguprogresstracker.DatabaseHelper;
import com.jln.wguprogresstracker.Note;
import com.jln.wguprogresstracker.R;

import static android.widget.TextView.BufferType.EDITABLE;

public class AddNoteActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    private EditText ETNote;
    private Spinner spinnerCourse;
    private Button buttonSave;
    private static boolean loaded = false;
    private Integer noteId;
    private Note mNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDb = DatabaseHelper.getInstance(this);

        buttonSave = (Button)findViewById(R.id.saveBtn);
        ETNote = (EditText)findViewById(R.id.noteET);
        spinnerCourse = (Spinner) findViewById(R.id.spinnerCourse);
        ArrayAdapter<String> spinnerCourseAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                myDb.getAllSpinnerContent("course_table"));
        spinnerCourseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourse.setAdapter(spinnerCourseAdapter);

        getIncomingIntent();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here are my course notes for " + spinnerCourse.getSelectedItem().toString() + ": " +ETNote.getText().toString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Course Notes for " + spinnerCourse.getSelectedItem().toString());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

    }

    public void AddData() {
        buttonSave.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {

                        boolean isInserted = myDb.insertNoteData(
                                spinnerCourse.getSelectedItem().toString(),
                                ETNote.getText().toString());
                        if(isInserted)
                            Toast.makeText(AddNoteActivity.this, "Course Note Data Inserted", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(AddNoteActivity.this, "Course Note Data Not Inserted", Toast.LENGTH_LONG).show();
                        finish();

                    }
                }
        );
    }

    public void UpdateData(final Integer id) {
        buttonSave.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {

                        boolean isUpdated = myDb.updateNoteData(id,
                                spinnerCourse.getSelectedItem().toString(),
                                ETNote.getText().toString());
                        if(isUpdated)
                            Toast.makeText(AddNoteActivity.this, "Course Note Data Updated", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(AddNoteActivity.this, "Course Note Data Not Updated", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
        );
    }
    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("NOTE_COL_2") && getIntent().hasExtra("NOTE_COL_3"))
        {
            noteId = getIntent().getIntExtra("NOTE_COL_1", -1);
            String courseName = getIntent().getStringExtra("NOTE_COL_2");
            String message = getIntent().getStringExtra("NOTE_COL_3");

            mNote = new Note(noteId, courseName, message);
            setNote(mNote);
            UpdateData(noteId);
            return true;
        }
        else {

            if(getIntent().hasExtra("COURSE_COL_2"))
                spinnerCourse.setSelection(myDb.getSpinnerPosition(spinnerCourse.getAdapter(),
                        getIntent().getStringExtra("COURSE_COL_2")));

            AddData();
            return false;
        }
    }
    private void setNote(Note nNote) {

        Integer coursePosition = myDb.getSpinnerPosition(spinnerCourse.getAdapter(), nNote.getCourseId());
        ETNote.setText(nNote.getMessage(), EDITABLE);
        spinnerCourse.setSelection(coursePosition);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (getIncomingIntent()) {
            Toolbar mActionBarToolbar = (Toolbar)findViewById(R.id.toolbar);
            mActionBarToolbar.setTitle("Modify Note");
            getMenuInflater().inflate(R.menu.menu_delete, menu);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this note?");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                myDb.deleteNote(noteId);
                finish();
                Toast.makeText(getApplicationContext(),"Note Deleted",
                        Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //  Action for 'NO' Button
                dialog.cancel();

            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.setTitle("Confirm Delete");
        alert.show();
        return super.onOptionsItemSelected(item);
    }

}
