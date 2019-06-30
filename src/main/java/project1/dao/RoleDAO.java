package project1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import project1.model.Role;
import project1.utils.DBConnector;
import project1.utils.ReimbursementLogger;

public class RoleDAO implements IRole {
  public static Logger logger = ReimbursementLogger.logger;
  public static Connection conn = DBConnector.getConnection();

  @Override
  public List<Role> getRolesForUser(int u_id) {
    List<Role> result = new ArrayList<>();
    try {
      String sql = "select * from roles where u_id = ? order by r_id";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, Integer.toString(u_id));
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        result.add(new Role(Integer.parseInt(rs.getString("r_id")), rs.getString("name"),
            rs.getString("can_approve_ri_basic").equals("1"), rs.getString("can_approve_ri_intermediate").equals("1"),
            rs.getString("can_approve_ri_super").equals("1"), Integer.parseInt(rs.getString("u_id"))));
      }
      return result;
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return null;
  }

  @Override
  public List<Role> getRolesForUser(String username) {
    List<Role> result = new ArrayList<>();
    try {
      String sql = "select * from roles where u_id = (select u_id from users where username = ?) order by r_id";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        result.add(new Role(Integer.parseInt(rs.getString("r_id")), rs.getString("name"),
            rs.getString("can_approve_ri_basic").equals("1"), rs.getString("can_approve_ri_intermediate").equals("1"),
            rs.getString("can_approve_ri_super").equals("1"), Integer.parseInt(rs.getString("u_id"))));
      }
      return result;
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return null;
  }

}