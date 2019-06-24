package project1.model;

public class Comment {
    private int c_id;
    private String title;
    private String body;
    private int u_id;
    private int r_id;

    public Comment(int c_id, String title, String body, int u_id, int r_id) {
        this.c_id = c_id;
        this.title = title;
        this.body = body;
        this.u_id = u_id;
        this.r_id = r_id;
    }

    public int getC_id() {
        return this.c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
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
