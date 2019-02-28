package com.jln.wguprogresstracker.Activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.jln.wguprogresstracker.AlarmHandler;
import com.jln.wguprogresstracker.Assessment;
import com.jln.wguprogresstracker.DatabaseHelper;
import com.jln.wguprogresstracker.DateUtil;
import com.jln.wguprogresstracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static android.widget.TextView.BufferType.EDITABLE;

//import com.thejenericwedding.thejenericwedding.AlarmHandler;

public class AddAssessment extends AppCompatActivity implements View.OnClickListener{

    private DatabaseHelper myDb;
    private EditText ETAssessmentName, ETDueDate, ETGoalDate;
    private CheckBox cbGoal;
    private Button buttonSave;
    private Spinner spinnerType, spinnerCourse;
    private DatePickerDialog dpdDue, dpdGoal;
    private TimePickerDialog tpdGoal;
    private TextView TVmessage;
    private Integer assessmentId;
    private Assessment mAssessment;
    private SimpleDateFormat dateFormat;
    private int nextAlarmId;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDb = DatabaseHelper.getInstance(this);

        ETAssessmentName = findViewById(R.id.assessmentNameET);
        ETDueDate = findViewById(R.id.dueDateET);
        ETGoalDate = findViewById(R.id.goalDateET);
        cbGoal = findViewById(R.id.goalDateAlertCB);
        buttonSave = findViewById(R.id.saveAssessmentBtn);

        // SET UP SPINNERS
        spinnerType = findViewById(R.id.spinnerType);
        ArrayAdapter<CharSequence> spinnerTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_type, android.R.layout.simple_spinner_item);
        spinnerTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(spinnerTypeAdapter);

        spinnerCourse = findViewById(R.id.spinnerCourse);
        ArrayAdapter<String> spinnerCourseAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                myDb.getAllSpinnerContent("course_table"));
        spinnerCourseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourse.setAdapter(spinnerCourseAdapter);

        dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        setupDatePickers();
        getIncomingIntent();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setupDatePickers(){

        ETDueDate.setOnClickListener(this);
        ETGoalDate.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        dpdDue = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                ETDueDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dpdGoal = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                ETGoalDate.setText(dateFormat.format(newDate.getTime()));
                tpdGoal = new TimePickerDialog(AddAssessment.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String AM_PM;
                        if (hourOfDay < 12) {
                            AM_PM = "AM";
                        }
                        else {
                            AM_PM = "PM";
                        }
                        if (hourOfDay > 12) {
                            hourOfDay = hourOfDay - 12;
                        }
                        if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        String minuteString = Integer.toString(minute);
                        if (minute < 10) {
                            minuteString = "0" + minuteString;
                        }
                        String datetime = ETGoalDate.getText().toString() + " " + hourOfDay + ":" + minuteString
                                + " " + AM_PM + " " + TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
                        ETGoalDate.setText(datetime);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                tpdGoal.show();
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        ETDueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dpdDue.show();
                }
            }
        });

        ETGoalDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dpdGoal.show();
                }
            }
        });
    }
    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("ASSESSMENT_COL_2") && getIntent().hasExtra("ASSESSMENT_COL_3") &&
                getIntent().hasExtra("ASSESSMENT_COL_4") && getIntent().hasExtra("ASSESSMENT_COL_5")
                && getIntent().hasExtra("ASSESSMENT_COL_6") && getIntent().hasExtra("ASSESSMENT_COL_7")
                && getIntent().hasExtra("ASSESSMENT_COL_8"))
        {
            assessmentId = getIntent().getIntExtra("ASSESSMENT_COL_1", -1);
            String type = getIntent().getStringExtra("ASSESSMENT_COL_2");
            String name = getIntent().getStringExtra("ASSESSMENT_COL_3");
            String dueDate = getIntent().getStringExtra("ASSESSMENT_COL_4");
            String goalDate = getIntent().getStringExtra("ASSESSMENT_COL_5");
            Integer goalDateAlert = getIntent().getIntExtra("ASSESSMENT_COL_6", -1);
            String courseFID = getIntent().getStringExtra("ASSESSMENT_COL_7");
            Integer alertCode = getIntent().getIntExtra("ASSESSMENT_COL_8", -1);

            mAssessment = new Assessment(assessmentId,courseFID,type, name, dueDate, goalDate, goalDateAlert, alertCode);

            setAssessment(mAssessment);
            UpdateData(assessmentId);
            return true;
        }
        else
        {
            // PRE-LOAD THE COURSE SPINNER IF THERE IS AN EXTRA (FROM ADDCOURSE ACTIVITY)
            if(getIntent().hasExtra("COURSE_COL_2"))
            {
                String courseName = getIntent().getStringExtra("COURSE_COL_2");
                spinnerCourse.setSelection(myDb.getSpinnerPosition(spinnerCourse.getAdapter(), courseName));
            }

            // IF NO COURSE, CANNOT ADD AN ASSESSMENT
            if(spinnerCourse.getCount() == 0) {
                TVmessage = (TextView) findViewById(R.id.textViewMessage);
                TVmessage.setText("Unable to add an Assessment without an added Course.");
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

    public void AddData() {
        buttonSave.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                Integer intGoal= 0;
                if(cbGoal.isChecked())
                    intGoal = 1;
                nextAlarmId = AlarmHandler.getNextAlarmId(AddAssessment.this);
                boolean isInserted = myDb.insertAssessmentData(spinnerType.getSelectedItem().toString(),
                        ETAssessmentName.getText().toString(),
                        ETDueDate.getText().toString(),
                        ETGoalDate.getText().toString(),
                        intGoal,
                        spinnerCourse.getSelectedItem().toString(), nextAlarmId);
                        AlarmHandler.incrementNextAlarmId(AddAssessment.this);

                    // create notification. Only create the notification if the checkbox is checked and the time is AFTER the current time.
                if (intGoal == 1 && DateUtil.getDateTimestamp(ETGoalDate.getText().toString()) > System.currentTimeMillis())
                    enableNotifications();

                    if(isInserted)
                    Toast.makeText(AddAssessment.this, "Assessment Data Inserted", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(AddAssessment.this, "Assessment Data Not Inserted", Toast.LENGTH_LONG).show();
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
                Integer intGoal= 0;
                if(cbGoal.isChecked())
                    intGoal = 1;
                boolean isUpdated = myDb.updateAssessmentData(id, spinnerType.getSelectedItem().toString(),
                        ETAssessmentName.getText().toString(),
                        ETDueDate.getText().toString(),
                        ETGoalDate.getText().toString(),
                        intGoal,
                        spinnerCourse.getSelectedItem().toString(), mAssessment.getAlarmCode());

                    // Create/cancel notification.
                    // Only create the notification if the checkbox is checked and the time is AFTER the current time.
                    // Cancel the notification if it is unchecked, but WAS checked.
                    if (intGoal == 1 && DateUtil.getDateTimestamp(ETGoalDate.getText().toString()) > System.currentTimeMillis())
                        enableNotifications();
                    else if (intGoal == 0 && mAssessment.getGoalDateAlert() == 1)
                        cancelNotifications();

                    if(isUpdated)
                    Toast.makeText(AddAssessment.this, "Assessment Data Updated", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(AddAssessment.this, "Assessment Data Not Updated", Toast.LENGTH_LONG).show();
                finish();
                }
            }
        );
    }

    private void enableNotifications()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmHandler.class );
        intent.putExtra("assessment_name", ETAssessmentName.getText().toString());
        intent.putExtra("course_name", spinnerCourse.getSelectedItem().toString());

        Integer alarmId;
        if (mAssessment != null)
            alarmId = mAssessment.getAlarmCode();
        else
            alarmId = nextAlarmId;

        intent.putExtra("id", alarmId);
        PendingIntent pendingIntent;

        pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, DateUtil.getDateTimestamp(ETGoalDate.getText().toString()), pendingIntent);
    }

    private void cancelNotifications()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmHandler.class );
        intent.putExtra("assessment_name", ETAssessmentName.getText().toString());
        intent.putExtra("course_name", spinnerCourse.getSelectedItem().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                mAssessment.getAlarmCode(), intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    private void setAssessment(Assessment nAssessment) {

        spinnerType.setSelection(myDb.getSpinnerPosition(spinnerType.getAdapter(), nAssessment.getType()));
        ETAssessmentName.setText(nAssessment.getTitle(), EDITABLE);
        ETGoalDate.setText(nAssessment.getGoalDate(), EDITABLE);
        cbGoal.setChecked(isChecked(nAssessment.getGoalDateAlert()));
        ETDueDate.setText(nAssessment.getDueDate(), EDITABLE);
        spinnerCourse.setSelection(myDb.getSpinnerPosition(spinnerCourse.getAdapter(), nAssessment.getCourseId()));
    }
    private Boolean isChecked(Integer i)
    {
        if (i == 1)
            return true;
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (getIncomingIntent()) {
            Toolbar mActionBarToolbar = (Toolbar)findViewById(R.id.toolbar);
            mActionBarToolbar.setTitle("Modify Assessment");
            getMenuInflater().inflate(R.menu.menu_delete, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this assessment?");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                myDb.deleteAssessment(mAssessment.getId());
                finish();
                Toast.makeText(getApplicationContext(),"Assessment Deleted",
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

    @Override
    public void onClick(View view) {
        if (view == ETDueDate) {
            dpdDue.show();
        }
        if (view == ETGoalDate) {
            dpdGoal.show();
        }
    }

}
