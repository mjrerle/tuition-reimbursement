package project1.dao;

import java.util.List;

import project1.model.User;

public interface IUser {
    public List<User> getUsers();
    public User getUser(String username, String password);
    public User getUser(String username);
    public User getUser(int u_id);
    public User getUserSupervisor(int u_id);
    public User getUserDepartmentHead(int u_id);
    public List<User> getUnderlings(int u_id);
}
