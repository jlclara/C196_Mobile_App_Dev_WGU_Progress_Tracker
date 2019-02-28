package com.jln.wguprogresstracker.Activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jln.wguprogresstracker.AlarmHandler;
import com.jln.wguprogresstracker.Course;
import com.jln.wguprogresstracker.DatabaseHelper;
import com.jln.wguprogresstracker.DateUtil;
import com.jln.wguprogresstracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.widget.TextView.BufferType.EDITABLE;

public class AddCourse extends AppCompatActivity implements View.OnClickListener{

    private DatabaseHelper myDb;
    private EditText ETcourseTitle, ETstartDate, ETendDate;
    private TextView TVmessage;
    private CheckBox cbStart, cbEnd;
    private Button buttonSave, buttonCourseAssessments, buttonCourseNotes;
    private Spinner spinnerStatus, spinnerMentor, spinnerTerm;
    private DatePickerDialog dpdStart, dpdEnd;
    private Integer courseId;
    private static boolean loaded = false;
    private Course mCourse;
    private SimpleDateFormat dateFormat;
    private int nextAlarmId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDb = DatabaseHelper.getInstance(this);

        buttonSave = (Button)findViewById(R.id.buttonSaveCourse);
        buttonCourseAssessments = (Button)findViewById(R.id.btnCourseAssessment);
        buttonCourseNotes = (Button)findViewById(R.id.btnCourseNotes);
        ETcourseTitle = (EditText)findViewById(R.id.editTextCourseName);
        ETstartDate = (EditText)findViewById(R.id.editTextStartDate);
        ETendDate = (EditText)findViewById(R.id.editTextEndDate);
        cbStart = (CheckBox)findViewById(R.id.checkboxStart);
        cbEnd = (CheckBox)findViewById(R.id.checkBoxEnd);

        // SET UP SPINNERS
        spinnerStatus = (Spinner)findViewById(R.id.spinnerStatus);
        ArrayAdapter<CharSequence> spinnerStatusAdapter =  ArrayAdapter.createFromResource(this,
                R.array.spinner_status, android.R.layout.simple_spinner_item);
        spinnerStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerStatusAdapter);

        spinnerMentor = (Spinner)findViewById(R.id.spinnerMentor);
        ArrayAdapter<String> spinnerMentorAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                myDb.getAllSpinnerContent("mentor_table"));
        spinnerMentor.setAdapter(spinnerMentorAdapter);

        spinnerTerm = (Spinner)findViewById(R.id.spinnerTerm);
        ArrayAdapter<String> spinnerTermAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                myDb.getAllSpinnerContent("term_table"));
        spinnerTerm.setAdapter(spinnerTermAdapter);

        dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        setupDatePickers();
        getIncomingIntent();
    }

    private void setupDatePickers(){
        ETstartDate.setOnClickListener(this);
        ETendDate.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        dpdStart = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                ETstartDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dpdEnd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                ETendDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        ETstartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dpdStart.show();
                }
            }
        });

        ETendDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dpdEnd.show();
                }
            }
        });
    }

    public void AddData() {
        buttonSave.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                Integer intStart= 0 , intEnd = 0;
                if(cbStart.isChecked())
                    intStart = 1;
                if (cbEnd.isChecked())
                    intEnd =1;

                nextAlarmId = AlarmHandler.getNextAlarmId(AddCourse.this);
                boolean isInserted = myDb.insertCourseData(ETcourseTitle.getText().toString(),
                        spinnerStatus.getSelectedItem().toString(),
                        ETstartDate.getText().toString(),
                        intStart,
                        ETendDate.getText().toString(),
                        intEnd,
                        spinnerMentor.getSelectedItem().toString(),
                        spinnerTerm.getSelectedItem().toString(), nextAlarmId);

                    // create notification. Only create the notification if the checkbox is checked and the time is AFTER the current time.
                    if (intStart == 1 && DateUtil.getDateTimestamp(ETstartDate.getText().toString()) > System.currentTimeMillis())
                        enableNotifications("start_alert_title");

                    if (intEnd == 1 && DateUtil.getDateTimestamp(ETendDate.getText().toString()) > System.currentTimeMillis())
                        enableNotifications("end_alert_title");

                    AlarmHandler.incrementNextAlarmId(AddCourse.this);
                    AlarmHandler.incrementNextAlarmId(AddCourse.this);


                    if(isInserted)
                    Toast.makeText(AddCourse.this, "Course Data Inserted", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(AddCourse.this, "Course Data Not Inserted", Toast.LENGTH_LONG).show();
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
                Integer intStart= 0 , intEnd = 0;
                if(cbStart.isChecked())
                    intStart = 1;
                if (cbEnd.isChecked())
                    intEnd =1;

                boolean isUpdated = myDb.updateCourseData(id, ETcourseTitle.getText().toString(),
                        spinnerStatus.getSelectedItem().toString(),
                        ETstartDate.getText().toString(),
                        intStart,
                        ETendDate.getText().toString(),
                        intEnd, mCourse.getNotesId(),
                        spinnerMentor.getSelectedItem().toString(),
                        mCourse.getAssessmentsId(),
                        spinnerTerm.getSelectedItem().toString(), mCourse.getAlertCode());

                // UPDATE ASSESSMENT_TABLE and NOTE_TABLE if the course name changed
                if (mCourse.getTitle() != ETcourseTitle.getText().toString()) {
                   myDb.updateCourseAssessment(mCourse.getTitle(), ETcourseTitle.getText().toString());
                   myDb.updateCourseNotes(mCourse.getTitle(), ETcourseTitle.getText().toString());
                }

                // Create/cancel notification
                // Only create the notification if the checkbox is checked and the time is AFTER the current time.
                // Cancel the notification if it is unchecked, but WAS checked.
                if (intStart == 1 && DateUtil.getDateTimestamp(ETstartDate.getText().toString()) > System.currentTimeMillis())
                    enableNotifications("start_alert_title");
                else if (intStart == 0 && mCourse.getStartDateAlert() == 1)
                    cancelNotifications("start_alert_title");

                if (intEnd == 1 && DateUtil.getDateTimestamp(ETendDate.getText().toString()) > System.currentTimeMillis())
                    enableNotifications("end_alert_title");
                else if (intEnd == 0 && mCourse.getEndDateAlert() == 1)
                    cancelNotifications("end_alert_title");

                if(isUpdated)
                    Toast.makeText(AddCourse.this, "Course Data Updated", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(AddCourse.this, "Course Data Not Updated", Toast.LENGTH_LONG).show();
                finish();
                }
            }
        );
    }

    private void enableNotifications(String startOrEnd)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmHandler.class );
        intent.putExtra("course_name", ETcourseTitle.getText().toString());
        intent.putExtra("term_name", spinnerTerm.getSelectedItem().toString());

        Integer alarmId = 0;
        String dateText = "";

        if (mCourse != null)
            alarmId = mCourse.getAlertCode();
        else
            alarmId = nextAlarmId;

        intent.putExtra("id", alarmId);

        if (startOrEnd == "end_alert_title") {
            alarmId++;
            intent.putExtra(startOrEnd, "Reminder: A course is ending today!");
            dateText = ETendDate.getText().toString();
        }
        else if (startOrEnd == "start_alert_title") {
            intent.putExtra(startOrEnd, "Reminder: A course is starting today!");
            dateText = ETstartDate.getText().toString();
        }

        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, DateUtil.getDate(dateText), pendingIntent);
    }

    private void cancelNotifications(String startOrEnd)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmHandler.class );
        intent.putExtra("course_name", ETcourseTitle.getText().toString());
        intent.putExtra("term_name", spinnerTerm.getSelectedItem().toString());
        intent.putExtra(startOrEnd, "");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                mCourse.getAlertCode(), intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("COURSE_COL_2") && getIntent().hasExtra("COURSE_COL_3") &&
                getIntent().hasExtra("COURSE_COL_4") && getIntent().hasExtra("COURSE_COL_5")
                && getIntent().hasExtra("COURSE_COL_6") && getIntent().hasExtra("COURSE_COL_7")
                && getIntent().hasExtra("COURSE_COL_8") && getIntent().hasExtra("COURSE_COL_9"))
        {
            courseId = getIntent().getIntExtra("COURSE_COL_1", -1);
            String titleText = getIntent().getStringExtra("COURSE_COL_2");
            String statusText = getIntent().getStringExtra("COURSE_COL_3");
            String startText = getIntent().getStringExtra("COURSE_COL_4");
            Integer startAlertCB = getIntent().getIntExtra("COURSE_COL_5", -1);
            String endText = getIntent().getStringExtra("COURSE_COL_6");
            Integer endAlertCB = getIntent().getIntExtra("COURSE_COL_7", -1);
            String notesId = getIntent().getStringExtra("COURSE_COL_8");
            String mentorId = getIntent().getStringExtra("COURSE_COL_9");
            String assessmentId = getIntent().getStringExtra("COURSE_COL_10");
            String courseTerm = getIntent().getStringExtra("COURSE_COL_11");
            Integer alertCode = getIntent().getIntExtra("COURSE_COL_12", -1);

            mCourse = new Course(courseId, courseTerm, titleText, statusText, startText,
                    startAlertCB, endText, endAlertCB, assessmentId, notesId, mentorId, alertCode);

            setCourse(mCourse);
            UpdateData(courseId);
            return true;
        }
        else {
            if(getIntent().hasExtra("TERM_COL_2"))
            {
                String termName = getIntent().getStringExtra("TERM_COL_2");
                spinnerTerm.setSelection(myDb.getSpinnerPosition(spinnerTerm.getAdapter(), termName));
            }

            buttonCourseAssessments.setVisibility(View.INVISIBLE);
            buttonCourseNotes.setVisibility(View.INVISIBLE);

            // DO NOT ALLOW ADDING A COURSE WITHOUT TERM AND MENTOR ADDED
            if (spinnerTerm.getCount() == 0 || spinnerMentor.getCount() == 0) {
                TVmessage = (TextView) findViewById(R.id.textViewMessage);
                TVmessage.setText("Unable to add a Course without an added Term and Mentor.");
                buttonSave.setText("Return to home screen.");
                buttonSave.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                            }
                        }
                );
                return false;
            }
            AddData();
            return false;
        }
    }

    private void setCourse(Course nCourse) {
        ETcourseTitle.setText(nCourse.getTitle(), EDITABLE);
        ETstartDate.setText(nCourse.getStartDate(), EDITABLE);
        cbStart.setChecked(isChecked(nCourse.getStartDateAlert()));
        cbEnd.setChecked(isChecked(nCourse.getEndDateAlert()));
        ETendDate.setText(nCourse.getEndDate(), EDITABLE);
        spinnerStatus.setSelection(myDb.getSpinnerPosition(spinnerStatus.getAdapter(), nCourse.getStatus()));
        spinnerMentor.setSelection(myDb.getSpinnerPosition(spinnerMentor.getAdapter(), nCourse.getMentorsId()));
        spinnerTerm.setSelection(myDb.getSpinnerPosition(spinnerTerm.getAdapter(), nCourse.getTermId()));
    }

    private Boolean isChecked(Integer i)
    {
        if (i == 1)
            return true;
        return false;
    }

    public void onCourseAssessmentBtnClicked(View v){
        Intent assessmentIntent = new Intent(getBaseContext(), CourseAssessments.class);
        assessmentIntent.putExtra("COURSE_COL_2", ETcourseTitle.getText().toString());
        startActivity(assessmentIntent);
    }

    public void onCourseNotesBtnClicked(View v){
        Intent notesIntent = new Intent(getBaseContext(), CourseNotes.class);
        notesIntent.putExtra("COURSE_COL_2", ETcourseTitle.getText().toString());
        startActivity(notesIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (getIncomingIntent()) {
            Toolbar mActionBarToolbar = (Toolbar)findViewById(R.id.toolbar);
            mActionBarToolbar.setTitle("Modify Course");
            getMenuInflater().inflate(R.menu.menu_delete, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // HANDLING DELETE COURSE

        // check to see if this course has associated assessments and notes
        String query = "SELECT * FROM assessment_table WHERE COURSE_FID = '"+ (mCourse.getTitle()) + "'";
        Cursor cursor = myDb.getWritableDatabase().rawQuery(query, null);
        String queryNote = "SELECT * FROM note_table WHERE NOTE_COURSE_ID = '" + (mCourse.getTitle()) + "'";
        Cursor cursorNote = myDb.getWritableDatabase().rawQuery(queryNote, null);

        // cursor.getCount() shows how many assessments are associated with this course. If not equal to 0, cannot delete this course.
        if(cursor.getCount() != 0) {
            Toast.makeText(getApplicationContext(), "You cannot delete a course with an associated assessment.",
                    Toast.LENGTH_LONG).show();
        }
        else if(cursorNote.getCount() != 0) {
            Toast.makeText(getApplicationContext(), "You cannot delete a course with an associated note.",
                    Toast.LENGTH_LONG).show();
        }
        else {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to delete this course?");
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    myDb.deleteCourse(mCourse.getId());
                    finish();
                    Toast.makeText(getApplicationContext(), "Course Deleted",
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
        if (view == ETstartDate) {
            dpdStart.show();
        }
        if (view == ETendDate) {
            dpdEnd.show();
        }
    }
}
