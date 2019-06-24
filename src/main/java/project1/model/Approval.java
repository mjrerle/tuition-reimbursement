package project1.model;

import java.util.Date;

public class Approval {
    private int a_id;
    private String approval_type;
    private Date approval_date;
    private String status;
    private int u_id;
    private int r_id;

    public Approval(int a_id, String approval_type, Date approval_date, String status, int u_id, int r_id) {
        this.a_id = a_id;
        this.approval_type = approval_type;
        this.approval_date = approval_date;
        this.status = status;
        this.u_id = u_id;
        this.r_id = r_id;
    }

    public int getA_id() {
        return this.a_id;
    }

    public void setA_id(int a_id) {
        this.a_id = a_id;
    }

    public String getApproval_type() {
        return this.approval_type;
    }

    public void setApproval_type(String approval_type) {
        this.approval_type = approval_type;
    }

    public Date getApproval_date() {
        return this.approval_date;
    }

    public void setApproval_date(Date approval_date) {
        this.approval_date = approval_date;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getU_id() {
        return this.u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public int getR_id() {
        return this.r_id;
    }

    public void setR_id(int r_id) {
        this.r_id = r_id;
    }

}
