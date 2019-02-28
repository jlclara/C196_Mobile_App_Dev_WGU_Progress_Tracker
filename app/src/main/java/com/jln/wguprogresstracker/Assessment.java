package com.jln.wguprogresstracker;

public class Assessment {

    private Integer id;
    private String courseId;
    private String type;
    private String title;
    private String dueDate;
    private String goalDate;
    private Integer goalDateAlert;
    private Integer alarmCode;



    public Assessment(Integer id, String courseId, String type, String title, String dueDate, String goalDate, Integer goalDateAlert, Integer alarmCode) {
        this.id = id;
        this.courseId = courseId;
        this.type = type;
        this.title = title;
        this.dueDate = dueDate;
        this.goalDate = goalDate;
        this.goalDateAlert = goalDateAlert;
        this.alarmCode = alarmCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(String goalDate) {
        this.goalDate = goalDate;
    }

    public Integer getGoalDateAlert() {
        return goalDateAlert;
    }

    public void setGoalDateAlert(Integer goalDateAlert) {
        this.goalDateAlert = goalDateAlert;
    }

    public Integer getAlarmCode() {
        return alarmCode;
    }

    public void setAlarmCode(Integer alarmCode) {
        this.alarmCode = alarmCode;
    }
}
