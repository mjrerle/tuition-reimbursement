package project1.service;

import java.util.List;

import project1.dao.UserDAO;
import project1.model.User;

public class UserService {
  public static UserDAO udao = new UserDAO();
  public static List<User> getUsers() {
    return udao.getUsers();
  }
  public static User getUser(String username) {
    return udao.getUser(username);
  }

  public static User getUser(int u_id) {
    return udao.getUser(u_id);
  }

  public static User getUser(String username, String password) {
    return udao.getUser(username, password);
  }

  public static User getUserSupervisor(int u_id) {
    return udao.getUserSupervisor(u_id);
  }

  public static User getUserDepartmentHead(int u_id) {
    return udao.getUserDepartmentHead(u_id);
  }

  public static List<User> getUnderlings(int u_id) {
    return udao.getUnderlings(u_id);
  }
}