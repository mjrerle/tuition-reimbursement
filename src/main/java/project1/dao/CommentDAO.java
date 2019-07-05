package project1.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import project1.model.Comment;
import project1.utils.DBConnector;
import project1.utils.ReimbursementLogger;

public class CommentDAO implements IComment {
  public static Logger logger = ReimbursementLogger.logger;
  public static Connection conn = DBConnector.getConnection();

  @Override
  public List<Comment> getComments() {
    List<Comment> result = new ArrayList<>();
    try {
      String sql = "select * from comments order by c_id";
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        result.add(new Comment(Integer.parseInt(rs.getString("c_id")), rs.getString("title"), rs.getString("body"),
            rs.getString("status"), Integer.parseInt(rs.getString("u_id")), Integer.parseInt(rs.getString("r_id"))));
      }
      return result;
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return null;
  }

  @Override
  public List<Comment> getCommentsByUserForReimbursement(int u_id, int r_id) {
    List<Comment> result = new ArrayList<>();
    try {
      String sql = "select * from comments where u_id = ? and r_id = ? order by c_id";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, Integer.toString(u_id));
      ps.setString(2, Integer.toString(r_id));
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        result.add(new Comment(Integer.parseInt(rs.getString("c_id")), rs.getString("title"), rs.getString("body"),
            rs.getString("status"), Integer.parseInt(rs.getString("u_id")), Integer.parseInt(rs.getString("r_id"))));
      }
      return result;
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return null;
  }

  @Override
  public List<Comment> getCommentsForReimbursement(int r_id) {
    List<Comment> result = new ArrayList<>();
    try {
      String sql = "select * from comments where r_id = ? order by c_id";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, Integer.toString(r_id));
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        result.add(new Comment(Integer.parseInt(rs.getString("c_id")), rs.getString("title"), rs.getString("body"),
            rs.getString("status"), Integer.parseInt(rs.getString("u_id")), Integer.parseInt(rs.getString("r_id"))));
      }
      return result;
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return null;
  }

  @Override
  public Comment getComment(int c_id) {
    try {
      String sql = "select * from comments where c_id = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, Integer.toString(c_id));
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        return new Comment(Integer.parseInt(rs.getString("c_id")), rs.getString("title"), rs.getString("body"),
            rs.getString("status"), Integer.parseInt(rs.getString("u_id")), Integer.parseInt(rs.getString("r_id")));
      }
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return null;
  }

  @Override
  public boolean createComment(Comment comment) {
    try {
      String sql = "call create_new_comment(?, ?, ?, ?, ?)";
      CallableStatement cs = conn.prepareCall(sql);
      cs.setString(1, comment.getTitle());
      cs.setString(2, comment.getBody());
      cs.setString(3, comment.getStatus());
      cs.setString(4, Integer.toString(comment.getU_id()));
      cs.setString(5, Integer.toString(comment.getR_id()));
      cs.execute();
      
      return true;
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return false;
  }

  @Override
  public boolean deleteComment(int c_id) {
    try {
      String sql = "delete from comments where c_id = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, Integer.toString(c_id));
      ps.execute();
      return true;
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return false;
  }

  @Override
  public boolean updateComment(Comment comment) {
    try {
      String sql = "update comments set title = ?, body = ?, status = ?, u_id = ?, r_id = ? where c_id = ?";
      CallableStatement cs = conn.prepareCall(sql);
      cs.setString(1, comment.getTitle());
      cs.setString(2, comment.getBody());
      cs.setString(3, comment.getStatus());
      cs.setString(4, Integer.toString(comment.getU_id()));
      cs.setString(5, Integer.toString(comment.getR_id()));
      cs.setString(6, Integer.toString(comment.getC_id()));
      cs.execute();
      return true;
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return false;
  }

}