package project1.model;

public class Notification {
    private int n_id;
    private String title;
    private String body;
    private int u_id;

    public Notification(int n_id, String title, String body, int u_id) {
        this.n_id = n_id;
        this.title = title;
        this.body = body;
        this.u_id = u_id;
    }

    public int getN_id() {
        return this.n_id;
    }

    public void setN_id(int n_id) {
        this.n_id = n_id;
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

}
