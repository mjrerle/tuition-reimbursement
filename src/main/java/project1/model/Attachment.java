package project1.model;

import java.util.Objects;

public class Attachment {
    private int a_id;
    private String title;
    private String type;
    private String src;
    private int u_id;
    private int r_id;

    public Attachment(int a_id, String title, String type, String src, int u_id, int r_id) {
        this.a_id = a_id;
        this.title = title;
        this.type = type;
        this.src = src;
        this.u_id = u_id;
        this.r_id = r_id;
    }

    public int getA_id() {
        return this.a_id;
    }

    public void setA_id(int a_id) {
        this.a_id = a_id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSrc() {
        return this.src;
    }

    public void setSrc(String src) {
        this.src = src;
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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Attachment)) {
            return false;
        }
        Attachment attachment = (Attachment) o;
        return a_id == attachment.a_id && Objects.equals(title, attachment.title) && Objects.equals(type, attachment.type) && Objects.equals(src, attachment.src) && u_id == attachment.u_id && r_id == attachment.r_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a_id, title, type, src, u_id, r_id);
    }
   
}
