package com.jln.wguprogresstracker.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.jln.wguprogresstracker.DatabaseHelper;
import com.jln.wguprogresstracker.Mentor;
import com.jln.wguprogresstracker.R;

import static android.widget.TextView.BufferType.EDITABLE;

public class AddMentorActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    private EditText mentorName, phoneNumber, emailAdress;
    private Button saveBtn;
    private Mentor mMentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mentor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDb = DatabaseHelper.getInstance(this);

        mentorName = (EditText)findViewById(R.id.mentorNameET);
        phoneNumber = (EditText)findViewById(R.id.phoneNumberET);
        emailAdress = (EditText)findViewById(R.id.emailAddressET);
        saveBtn = (Button)findViewById(R.id.saveBtn);


        getIncomingIntent();

    }

    public void AddData() {
        saveBtn.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    boolean isInserted = myDb.insertMentorData(mentorName.getText().toString(),
                            phoneNumber.getText().toString(),
                            emailAdress.getText().toString());
                    if(isInserted)
                        Toast.makeText(AddMentorActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(AddMentorActivity.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
                    finish();

                }

            }
        );
    }
    public void UpdateData(final Integer id){
        saveBtn.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    boolean isUpdated = myDb.updateMentorData(id, mentorName.getText().toString(),
                            phoneNumber.getText().toString(),
                            emailAdress.getText().toString());

                    // update the COURSE_TABLE mentor column if the mentor name changed
                    if (mMentor.getName() != mentorName.getText().toString()) {
                        myDb.updateMentorCourses(mMentor.getName(), mentorName.getText().toString());
                    }
                    if(isUpdated)
                        Toast.makeText(AddMentorActivity.this, "Mentor Data Updated", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(AddMentorActivity.this, "Mentor Data Not Updated", Toast.LENGTH_LONG).show();
                    finish();

                }

            }
        );
    }

    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("MENTOR_COL_1") && getIntent().hasExtra("MENTOR_COL_2")
                && getIntent().hasExtra("MENTOR_COL_3") && getIntent().hasExtra("MENTOR_COL_4"))
        {
            Integer id = getIntent().getIntExtra("MENTOR_COL_1", -1);
            String nameText = getIntent().getStringExtra("MENTOR_COL_2");
            String phoneNumberText = getIntent().getStringExtra("MENTOR_COL_3");
            String emailAddressText = getIntent().getStringExtra("MENTOR_COL_4");

            mMentor = new Mentor(id, nameText, emailAddressText, phoneNumberText);
            setTerm(mMentor);
            UpdateData(id);
            return true;
        }
        else {
            AddData();
            return false;
        }
    }

    private void setTerm(Mentor nMentor) {
        mentorName.setText(nMentor.getName(), EDITABLE);
        phoneNumber.setText(nMentor.getPhoneNumber(), EDITABLE);
        emailAdress.setText(nMentor.getEmailAddress(), EDITABLE);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Mentors.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (getIncomingIntent()) {
            Toolbar mActionBarToolbar = (Toolbar)findViewById(R.id.toolbar);
            mActionBarToolbar.setTitle("Modify Mentor");
            getMenuInflater().inflate(R.menu.menu_delete, menu);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // HANDLING DELETE MENTOR

        // check to see if this term has an associated course
        String query = "SELECT * FROM course_table WHERE MENTOR = '"+ (mMentor.getName()) + "'";
        Cursor cursor = myDb.getWritableDatabase().rawQuery(query, null);

        if(cursor.getCount() != 0) {
            Toast.makeText(getApplicationContext(), "You cannot delete a mentor with an associated course.",
                    Toast.LENGTH_LONG).show();
        }

        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to delete this mentor?");
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    myDb.deleteCourse(mMentor.getMentorId());
                    finish();
                    Toast.makeText(getApplicationContext(), "Mentor Deleted",
                            Toast.LENGTH_LONG).show();
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
        }
        return super.onOptionsItemSelected(item);

    }


}
