package project1.dao;

import java.util.List;

import project1.model.Role;

public interface IRole {
  public List<Role> getRolesForUser(int u_id);
  public List<Role> getRolesForUser(String username);
} 