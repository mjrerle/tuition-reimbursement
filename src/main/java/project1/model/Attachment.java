package project1.model;

public class Attachment {
    private int a_id;
    private int uuid;
    private String title;
    private String type;
    private String src;
    private int r_id;

    public Attachment(int a_id, int uuid, String title, String type, String src, int r_id) {
        this.a_id = a_id;
        this.uuid = uuid;
        this.title = title;
        this.type = type;
        this.src = src;
        this.r_id = r_id;
    }

    public int getA_id() {
        return this.a_id;
    }

    public void setA_id(int a_id) {
        this.a_id = a_id;
    }

    public int getUuid() {
        return this.uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
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

    public int getR_id() {
        return this.r_id;
    }

    public void setR_id(int r_id) {
        this.r_id = r_id;
    }

}
