package project1.dao;

import project1.model.User;

public interface IUser {
    public User getUser(String username, String password);
    public User getUser(String username);
    public User getUser(int u_id);
}
