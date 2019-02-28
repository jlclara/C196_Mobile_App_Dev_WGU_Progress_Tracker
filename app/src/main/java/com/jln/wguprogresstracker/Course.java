package com.jln.wguprogresstracker;

public class Course {

    private Integer id;
    private String termId;
    private String title;
    private String status;
    private String startDate;
    private Integer startDateAlert;
    private String endDate;
    private Integer endDateAlert;
    private String assessmentsId;
    private String notesId;
    private String mentors;
    private Integer alertCode;

    public Course(){

    }
    public Course(Integer id, String termId, String title, String status, String startDate,
                  Integer startDateAlert, String endDate, Integer endDateAlert,
                  String assessmentsId, String notesId, String mentors, Integer alertCode) {
        this.id = id;
        this.termId = termId;
        this.title = title;
        this.status = status;
        this.startDate = startDate;
        this.startDateAlert = startDateAlert;
        this.endDate = endDate;
        this.endDateAlert = endDateAlert;
        this.assessmentsId = assessmentsId;
        this.notesId = notesId;
        this.mentors = mentors;
        this.alertCode = alertCode;
    }

    public Course(String termId, String title, String status, String startDate,
                  Integer startDateAlert, String endDate, Integer endDateAlert,
                  String assessmentsId, String notesId, String mentors, Integer alertCode) {
        this.termId = termId;
        this.title = title;
        this.status = status;
        this.startDate = startDate;
        this.startDateAlert = startDateAlert;
        this.endDate = endDate;
        this.endDateAlert = endDateAlert;
        this.assessmentsId = assessmentsId;
        this.notesId = notesId;
        this.mentors = mentors;
        this.alertCode = alertCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Integer getStartDateAlert() {
        return startDateAlert;
    }

    public void setStartDateAlert(Integer startDateAlert) {
        this.startDateAlert = startDateAlert;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getEndDateAlert() {
        return endDateAlert;
    }

    public void setEndDateAlert(Integer endDateAlert) {
        this.endDateAlert = endDateAlert;
    }

    public String getAssessmentsId() {
        return assessmentsId;
    }

    public void setAssessmentsId(String assessmentsId) {
        this.assessmentsId = assessmentsId;
    }

    public String getNotesId() {
        return notesId;
    }

    public void setNotesId(String notesId) {
        this.notesId = notesId;
    }

    public String getMentorsId() {
        return mentors;
    }

    public void setMentorsId(String mentors) {
        this.mentors = mentors;
    }

    public Integer getAlertCode() {
        return alertCode;
    }

    public void setAlertCode(Integer alertCode) {
        this.alertCode = alertCode;
    }
}
