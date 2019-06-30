package project1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import project1.utils.DBConnector;
import project1.utils.ReimbursementLogger;

import org.apache.log4j.Logger;
import project1.model.User;

public class UserDAO implements IUser {
    public static Logger logger = ReimbursementLogger.logger;
    public static Connection conn = DBConnector.getConnection();

    @Override
    public User getUser(String username, String password) {
        try {
            String sql = "select * from users where username = ? and password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new User(Integer.parseInt(rs.getString("u_id")), rs.getString("username"),
                        rs.getString("password"), rs.getString("email_address"),
                        Integer.parseInt(rs.getString("supervisor_id")),
                        Integer.parseInt(rs.getString("department_head_id")), rs.getString("department"));
            }
        } catch (SQLException e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    @Override
    public User getUser(String username) {
        try {
            String sql = "select * from users where username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new User(Integer.parseInt(rs.getString("u_id")), rs.getString("username"),
                        rs.getString("password"), rs.getString("email_address"),
                        Integer.parseInt(rs.getString("supervisor_id")),
                        Integer.parseInt(rs.getString("department_head_id")), rs.getString("department"));
            }
        } catch (SQLException e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    @Override
    public User getUser(int u_id) {
        try {
            String sql = "select * from users where u_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, Integer.toString(u_id));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new User(Integer.parseInt(rs.getString("u_id")), rs.getString("username"),
                        rs.getString("password"), rs.getString("email_address"),
                        Integer.parseInt(rs.getString("supervisor_id")),
                        Integer.parseInt(rs.getString("department_head_id")), rs.getString("department"));
            }
        } catch (SQLException e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    @Override
    public User getUserSupervisor(int u_id) {
        try {
            String sql = "select * from users where u_id = (select supervisor_id from users where u_id = ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, Integer.toString(u_id));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new User(Integer.parseInt(rs.getString("u_id")), rs.getString("username"),
                        rs.getString("password"), rs.getString("email_address"),
                        Integer.parseInt(rs.getString("supervisor_id")),
                        Integer.parseInt(rs.getString("department_head_id")), rs.getString("department"));
            }
        } catch (SQLException e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    @Override
    public User getUserDepartmentHead(int u_id) {
        try {
            String sql = "select * from users where u_id = (select department_head_id from users where u_id = ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, Integer.toString(u_id));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new User(Integer.parseInt(rs.getString("u_id")), rs.getString("username"),
                        rs.getString("password"), rs.getString("email_address"),
                        Integer.parseInt(rs.getString("supervisor_id")),
                        Integer.parseInt(rs.getString("department_head_id")), rs.getString("department"));
            }
        } catch (SQLException e) {
            logger.warn(e.getMessage());
        }
        return null;
    }
}
