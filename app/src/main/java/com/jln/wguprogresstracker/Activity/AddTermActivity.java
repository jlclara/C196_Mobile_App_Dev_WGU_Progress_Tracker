package com.jln.wguprogresstracker.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jln.wguprogresstracker.DatabaseHelper;
import com.jln.wguprogresstracker.R;
import com.jln.wguprogresstracker.Term;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.widget.TextView.BufferType.EDITABLE;

public class AddTermActivity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseHelper myDb;
    private EditText termName, startDate, endDate;
    private Button saveBtn, termCourseBtn;
    private DatePickerDialog dpdStart, dpdEnd;
    private Term mTerm;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDb = DatabaseHelper.getInstance(this);

        termName = (EditText)findViewById(R.id.termNameET);
        startDate = (EditText)findViewById(R.id.startDateET);
        endDate = (EditText)findViewById(R.id.endDateET);
        saveBtn = (Button)findViewById(R.id.saveBtn);
        termCourseBtn = (Button)findViewById(R.id.buttonTermCourses);

        dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

        setupDatePickers();
        getIncomingIntent();
    }

    private void setupDatePickers(){
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        dpdStart = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                startDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dpdEnd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                endDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dpdStart.show();
                }
            }
        });

        endDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dpdEnd.show();
                }
            }
        });
    }

    public void AddData() {
        saveBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                    boolean isInserted = myDb.insertTermData(termName.getText().toString(),
                           startDate.getText().toString(),
                            endDate.getText().toString());
                    if(isInserted)
                        Toast.makeText(AddTermActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(AddTermActivity.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
                    finish();

                    }

                }
        );
    }
    public void UpdateData(final Integer id) {
        saveBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        boolean isUpdated = myDb.updateTermData(id, termName.getText().toString(),
                                startDate.getText().toString(),
                                endDate.getText().toString());

                        // update the COURSE_TABLE term column if the term name changed
                        if( mTerm.getName()!= termName.getText().toString())
                           myDb.updateTermCourses(mTerm.getName(), termName.getText().toString());

                        if(isUpdated)
                            Toast.makeText(AddTermActivity.this, "Term Data Updated", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(AddTermActivity.this, "Term Data Not Updated", Toast.LENGTH_LONG).show();
                        finish();
                    }

                }
        );
    }

    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("TERM_COL_2") && getIntent().hasExtra("TERM_COL_3") && getIntent().hasExtra("TERM_COL_4"))
        {
            Integer id = getIntent().getIntExtra("TERM_COL_1", -1);
            String nameText = getIntent().getStringExtra("TERM_COL_2");
            String startText = getIntent().getStringExtra("TERM_COL_3");
            String endText = getIntent().getStringExtra("TERM_COL_4");

            mTerm = new Term(id, nameText, startText, endText);
            setTerm(mTerm);
            UpdateData(id);
            return true;
        }
        else {
            termCourseBtn.setVisibility(View.INVISIBLE);
            AddData();
            return false;
        }
    }

    private void setTerm(Term mTerm) {

        termName.setText(mTerm.getName(),EDITABLE);
        startDate.setText(mTerm.getStartDate(), EDITABLE);
        endDate.setText(mTerm.getEndDate(), EDITABLE);
    }

    public void onTermCourseBtnClicked(View v){
        Intent termCoursesIntent = new Intent(getBaseContext(), TermCourses.class);
        termCoursesIntent.putExtra("TERM_COL_2", mTerm.getName());
        startActivity(termCoursesIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (getIncomingIntent()) {
            Toolbar mActionBarToolbar = (Toolbar)findViewById(R.id.toolbar);
            mActionBarToolbar.setTitle("Modify Term");
            getMenuInflater().inflate(R.menu.menu_delete, menu);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // HANDLING DELETE TERM

        // check to see if this term has an associated course
        String query = "SELECT * FROM course_table WHERE COURSE_TERM_FID = '"+ (mTerm.getName()) + "'";
        Cursor cursor = myDb.getWritableDatabase().rawQuery(query, null);

        if(cursor.getCount() != 0) {
            Toast.makeText(getApplicationContext(), "You cannot delete a term with an associated course.",
                    Toast.LENGTH_LONG).show();
        }

        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to delete this term?");
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    myDb.deleteCourse(mTerm.getId());
                    finish();
                    Toast.makeText(getApplicationContext(), "Term Deleted",
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

    @Override
    public void onClick(View view) {
        if (view == startDate) {
            dpdStart.show();
        }
        if (view == endDate) {
            dpdEnd.show();
        }
    }
}
