package project1.model;

import java.util.Objects;

public class User {
    private int u_id;
    private String username;
    private String password;
    private String email_address;
    private int supervisor_id;
    private int department_head_id;
    private String department;

    public User(int u_id, String username, String password, String email_address, int supervisor_id,
            int department_head_id, String department) {
        this.u_id = u_id;
        this.username = username;
        this.password = password;
        this.email_address = email_address;
        this.supervisor_id = supervisor_id;
        this.department_head_id = department_head_id;
        this.department = department;
    }

    public int getU_id() {
        return this.u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail_address() {
        return this.email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public int getSupervisor_id() {
        return this.supervisor_id;
    }

    public void setSupervisor_id(int supervisor_id) {
        this.supervisor_id = supervisor_id;
    }

    public int getDepartment_head_id() {
        return this.department_head_id;
    }

    public void setDepartment_head_id(int department_head_id) {
        this.department_head_id = department_head_id;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return u_id == user.u_id && Objects.equals(username, user.username) && Objects.equals(password, user.password)
                && Objects.equals(email_address, user.email_address) && supervisor_id == user.supervisor_id
                && department_head_id == user.department_head_id && Objects.equals(department, user.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(u_id, username, password, email_address, supervisor_id, department_head_id, department);
    }

}
