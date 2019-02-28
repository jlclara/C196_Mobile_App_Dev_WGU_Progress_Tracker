package com.jln.wguprogresstracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static DatabaseHelper mInstance = null;
    public static final String DATABASE_NAME = "wgu_progress_tracker.db";
    public static final String TERM_TABLE_NAME = "term_table";
    public static final String TERM_COL_1 = "ID";
    public static final String TERM_COL_2 = "NAME";
    public static final String TERM_COL_3 = "START_DATE";
    public static final String TERM_COL_4 = "END_DATE";

    public static final String MENTOR_TABLE_NAME = "mentor_table";
    public static final String MENTOR_COL_1 = "ID";
    public static final String MENTOR_COL_2 = "NAME";
    public static final String MENTOR_COL_3 = "PHONE_NUMBER";
    public static final String MENTOR_COL_4 = "EMAIL_ADDRESS";

    public static final String COURSE_TABLE_NAME = "course_table";
    public static final String COURSE_COL_1 = "ID";
    public static final String COURSE_COL_2 = "COURSE_TITLE";
    public static final String COURSE_COL_3 = "COURSE_STATUS";
    public static final String COURSE_COL_4 = "COURSE_START_DATE";
    public static final String COURSE_COL_5 = "START_ALERT";
    public static final String COURSE_COL_6 = "COURSE_END_DATE";
    public static final String COURSE_COL_7 = "END_ALERT";
    public static final String COURSE_COL_8 = "NOTES_ID";
    public static final String COURSE_COL_9 = "MENTOR";
    public static final String COURSE_COL_10 = "ASSESSMENT_ID";
    public static final String COURSE_COL_11 = "COURSE_TERM_FID";
    public static final String COURSE_COL_12 = "ALERT_CODE";


    public static final String ASSESSMENT_TABLE_NAME = "assessment_table";
    public static final String ASSESSMENT_COL_1 = "ID";
    public static final String ASSESSMENT_COL_2 = "ASSESSMENT_TYPE";
    public static final String ASSESSMENT_COL_3 = "ASSESSMENT_TITLE";
    public static final String ASSESSMENT_COL_4 = "DUE_DATE";
    public static final String ASSESSMENT_COL_5 = "GOAL_DATE";
    public static final String ASSESSMENT_COL_6 = "GOAL_DATE_ALERT";
    public static final String ASSESSMENT_COL_7 = "COURSE_FID";
    public static final String ASSESSMENT_COL_8 = "ALERT_CODE";

    public static final String NOTE_TABLE_NAME = "note_table";
    public static final String NOTE_COL_1 = "ID";
    public static final String NOTE_COL_2 = "NOTE_COURSE_ID";
    public static final String NOTE_COL_3 = "NOTE_MESSAGE";

    public static DatabaseHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TERM_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NAME TEXT, START_DATE TEXT, END_DATE TEXT)");

        db.execSQL("create table " + MENTOR_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NAME TEXT,PHONE_NUMBER TEXT, EMAIL_ADDRESS TEXT)");

        db.execSQL("create table " + COURSE_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "COURSE_TITLE TEXT, COURSE_STATUS TEXT, COURSE_START_DATE TEXT, START_ALERT INTEGER, " +
                "COURSE_END_DATE TEXT, END_ALERT INTEGER, NOTES_ID TEXT, MENTOR TEXT, " +
                "ASSESSMENT_ID TEXT, COURSE_TERM_FID TEXT, ALERT_CODE INTEGER, " +
                "FOREIGN KEY(COURSE_TERM_FID) REFERENCES term_table(ID))");

        db.execSQL("create table " + ASSESSMENT_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ASSESSMENT_TYPE TEXT, ASSESSMENT_TITLE TEXT, DUE_DATE TEXT, GOAL_DATE TEXT, " +
                "GOAL_DATE_ALERT INTEGER, COURSE_FID TEXT, ALERT_CODE INTEGER, " +
                "FOREIGN KEY(COURSE_FID) REFERENCES COURSE_TABLE(ID))");

        db.execSQL("create table " + NOTE_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NOTE_COURSE_ID TEXT, NOTE_MESSAGE TEXT, " +
                "FOREIGN KEY(NOTE_COURSE_ID) REFERENCES COURSE_TABLE(ID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TERM_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MENTOR_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + COURSE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ASSESSMENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE_NAME);

        onCreate(db);
    }

    public boolean insertTermData(String name, String startDate, String endDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TERM_COL_2, name);
        contentValues.put(TERM_COL_3, startDate);
        contentValues.put(TERM_COL_4, endDate);
        long result = db.insert(TERM_TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updateTermData(Integer ID, String name, String startDate, String endDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TERM_COL_2, name);
        contentValues.put(TERM_COL_3, startDate);
        contentValues.put(TERM_COL_4, endDate);
        long result = db.update(TERM_TABLE_NAME, contentValues, "ID="+ ID, null);
        if(result == -1)
            return false;
        else
            return true;
    }

    public  boolean updateTermCourses(String oldTermName, String newTermName)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_COL_11, newTermName);
        long result = db.update(COURSE_TABLE_NAME, contentValues, "COURSE_TERM_FID = '" + oldTermName + "'", null);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertMentorData(String name, String phoneNumber, String emailAddress)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MENTOR_COL_2, name);
        contentValues.put(MENTOR_COL_3, phoneNumber);
        contentValues.put(MENTOR_COL_4, emailAddress);
        long result = db.insert(MENTOR_TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updateMentorData(Integer ID, String name, String phoneNumber, String emailAddress)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MENTOR_COL_2, name);
        contentValues.put(MENTOR_COL_3, phoneNumber);
        contentValues.put(MENTOR_COL_4, emailAddress);
        long result = db.update(MENTOR_TABLE_NAME, contentValues, "ID="+ ID, null);
        if(result == -1)
            return false;
        else
            return true;
    }

    public  boolean updateMentorCourses(String oldMentorName, String newMentorName)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_COL_9, newMentorName);
        long result = db.update(COURSE_TABLE_NAME, contentValues, "MENTOR = '" + oldMentorName + "'", null);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertCourseData(String title, String status, String startDate,
                                    Integer startAlert, String endDate, Integer endAlert,
                                    String mentor, String termId, Integer alertCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_COL_2, title);
        contentValues.put(COURSE_COL_3, status);
        contentValues.put(COURSE_COL_4, startDate);
        contentValues.put(COURSE_COL_5, startAlert);
        contentValues.put(COURSE_COL_6, endDate);
        contentValues.put(COURSE_COL_7, endAlert);
        contentValues.put(COURSE_COL_9, mentor);
        contentValues.put(COURSE_COL_11, termId);
        contentValues.put(COURSE_COL_12, alertCode);


        long result = db.insert(COURSE_TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
}

    public boolean updateCourseData(Integer ID, String title, String status, String startDate,
                                    Integer startAlert, String endDate, Integer endAlert,
                                    String notesId, String mentor, String assessmentId,
                                    String termId, Integer alertCode)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_COL_2, title);
        contentValues.put(COURSE_COL_3, status);
        contentValues.put(COURSE_COL_4, startDate);
        contentValues.put(COURSE_COL_5, startAlert);
        contentValues.put(COURSE_COL_6, endDate);
        contentValues.put(COURSE_COL_7, endAlert);
        contentValues.put(COURSE_COL_8, notesId);
        contentValues.put(COURSE_COL_9, mentor);
        contentValues.put(COURSE_COL_10, assessmentId);
        contentValues.put(COURSE_COL_11, termId);
        contentValues.put(COURSE_COL_12, alertCode);

        long result = db.update(COURSE_TABLE_NAME, contentValues, "ID="+ ID, null);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updateCourseAssessment(String oldCourseName, String newCourseName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ASSESSMENT_COL_7, newCourseName);
        long result = db.update(ASSESSMENT_TABLE_NAME, contentValues, ASSESSMENT_COL_7 + " = '" + oldCourseName + "'", null);
        if(result == -1)
            return false;
        else
            return true;

    }

    public boolean updateCourseNotes(String oldCourseName, String newCourseName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_COL_2, newCourseName);
        long result = db.update(NOTE_TABLE_NAME, contentValues, NOTE_COL_2 + " = '" + oldCourseName + "'", null);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean deleteCourse(Integer ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(COURSE_TABLE_NAME, "ID="+ ID, null);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertAssessmentData(String type, String title, String dueDate, String goalDate,
                                        Integer goalDateAlert, String courseFID, Integer alertCode)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ASSESSMENT_COL_2, type);
        contentValues.put(ASSESSMENT_COL_3, title);
        contentValues.put(ASSESSMENT_COL_4, dueDate);
        contentValues.put(ASSESSMENT_COL_5, goalDate);
        contentValues.put(ASSESSMENT_COL_6, goalDateAlert);
        contentValues.put(ASSESSMENT_COL_7, courseFID);
        contentValues.put(ASSESSMENT_COL_8, alertCode);

        long result = db.insert(ASSESSMENT_TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updateAssessmentData(Integer ID, String type, String title, String dueDate,
                                        String goalDate, Integer goalDateAlert, String courseFID, Integer alertCode)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ASSESSMENT_COL_2, type);
        contentValues.put(ASSESSMENT_COL_3, title);
        contentValues.put(ASSESSMENT_COL_4, dueDate);
        contentValues.put(ASSESSMENT_COL_5, goalDate);
        contentValues.put(ASSESSMENT_COL_6, goalDateAlert);
        contentValues.put(ASSESSMENT_COL_7, courseFID);
        contentValues.put(ASSESSMENT_COL_8, alertCode);

        long result = db.update(ASSESSMENT_TABLE_NAME, contentValues, "ID="+ ID, null);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean deleteAssessment(Integer ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(ASSESSMENT_TABLE_NAME, "ID="+ ID, null);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertNoteData(String courseID, String message)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_COL_2, courseID);
        contentValues.put(NOTE_COL_3, message);
        long result = db.insert(NOTE_TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updateNoteData(Integer ID, String courseID, String message)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_COL_2, courseID);
        contentValues.put(NOTE_COL_3, message);
        long result = db.update(NOTE_TABLE_NAME, contentValues, "ID="+ ID, null);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean deleteNote(Integer ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(NOTE_TABLE_NAME, "ID="+ ID, null);
        if(result == -1)
            return false;
        else
            return true;
    }

    // used to populate spinners in AddX activities
    public ArrayList<String> getAllSpinnerContent(String tableName) {
        String query = "Select * from " + tableName;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<String> spinnerContent = new ArrayList<String>();
        if(cursor.moveToFirst()){
            do{
                spinnerContent.add(cursor.getString(1));
            }while(cursor.moveToNext());
        }
        cursor.close();

        return spinnerContent;

    }

    public Integer getSpinnerPosition (SpinnerAdapter s, String v)
    {
        for (int i = 0; i < s.getCount(); i++)
            if (s.getItem(i).toString().equals(v))
                return i;
        return -1;
    }
}
