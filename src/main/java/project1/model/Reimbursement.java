package project1.model;

import java.util.Date;

public class Reimbursement {
    private int r_id;
    private String event_type;
    private double percent_coverage;
    private String status;
    private Date submission_date;
    private Date event_start_date;
    private Date event_end_date;
    private Date event_daily_start_time;
    private String event_address;
    private String event_description;
    private double amount_requested;
    private double amount_granted;
    private String event_grading_format;
    private String event_passing_grade;
    private String justification_comment;
    private int u_id;

    public Reimbursement(int r_id, String event_type, double percent_coverage, String status, Date submission_date,
            Date event_start_date, Date event_end_date, Date event_daily_start_time, String event_address,
            String event_description, double amount_requested, double amount_granted, String event_grading_format,
            String event_passing_grade, String justification_comment, int u_id) {
        this.r_id = r_id;
        this.event_type = event_type;
        this.percent_coverage = percent_coverage;
        this.status = status;
        this.submission_date = submission_date;
        this.event_start_date = event_start_date;
        this.event_end_date = event_end_date;
        this.event_daily_start_time = event_daily_start_time;
        this.event_address = event_address;
        this.event_description = event_description;
        this.amount_requested = amount_requested;
        this.amount_granted = amount_granted;
        this.event_grading_format = event_grading_format;
        this.event_passing_grade = event_passing_grade;
        this.justification_comment = justification_comment;
        this.u_id = u_id;
    }

    public int getR_id() {
        return this.r_id;
    }

    public void setR_id(int r_id) {
        this.r_id = r_id;
    }

    public String getEvent_type() {
        return this.event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public double getPercent_coverage() {
        return this.percent_coverage;
    }

    public void setPercent_coverage(double percent_coverage) {
        this.percent_coverage = percent_coverage;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getSubmission_date() {
        return this.submission_date;
    }

    public void setSubmission_date(Date submission_date) {
        this.submission_date = submission_date;
    }

    public Date getEvent_start_date() {
        return this.event_start_date;
    }

    public void setEvent_start_date(Date event_start_date) {
        this.event_start_date = event_start_date;
    }

    public Date getEvent_end_date() {
        return this.event_end_date;
    }

    public void setEvent_end_date(Date event_end_date) {
        this.event_end_date = event_end_date;
    }

    public Date getEvent_daily_start_time() {
        return this.event_daily_start_time;
    }

    public void setEvent_daily_start_time(Date event_daily_start_time) {
        this.event_daily_start_time = event_daily_start_time;
    }

    public String getEvent_address() {
        return this.event_address;
    }

    public void setEvent_address(String event_address) {
        this.event_address = event_address;
    }

    public String getEvent_description() {
        return this.event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public double getAmount_requested() {
        return this.amount_requested;
    }

    public void setAmount_requested(double amount_requested) {
        this.amount_requested = amount_requested;
    }

    public double getAmount_granted() {
        return this.amount_granted;
    }

    public void setAmount_granted(double amount_granted) {
        this.amount_granted = amount_granted;
    }

    public String getEvent_grading_format() {
        return this.event_grading_format;
    }

    public void setEvent_grading_format(String event_grading_format) {
        this.event_grading_format = event_grading_format;
    }

    public String getEvent_passing_grade() {
        return this.event_passing_grade;
    }

    public void setEvent_passing_grade(String event_passing_grade) {
        this.event_passing_grade = event_passing_grade;
    }

    public String getJustification_comment() {
        return this.justification_comment;
    }

    public void setJustification_comment(String justification_comment) {
        this.justification_comment = justification_comment;
    }

    public int getU_id() {
        return this.u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

}
