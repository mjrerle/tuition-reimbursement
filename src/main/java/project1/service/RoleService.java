package project1.service;

import java.util.List;

import project1.dao.RoleDAO;
import project1.model.Role;

public class RoleService {
  public static RoleDAO rdao = new RoleDAO();

  public static List<Role> getRolesForUser(int u_id) {
    return rdao.getRolesForUser(u_id);
  }

  public static List<Role> getRolesForUser(String username) {
    return rdao.getRolesForUser(username);
  }
}