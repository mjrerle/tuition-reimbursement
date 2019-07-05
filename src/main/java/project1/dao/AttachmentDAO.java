package project1.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import project1.model.Attachment;
import project1.utils.DBConnector;
import project1.utils.ReimbursementLogger;

public class AttachmentDAO implements IAttachment {
  public static Logger logger = ReimbursementLogger.logger;
  public static Connection conn = DBConnector.getConnection();

  @Override
  public List<Attachment> getAttachments() {
    List<Attachment> result = new ArrayList<>();
    try {
      String sql = "select * from attachments order by a_id";
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        result.add(new Attachment(Integer.parseInt(rs.getString("a_id")), rs.getString("title"), rs.getString("type"),
            rs.getString("src"), Integer.parseInt(rs.getString("u_id")), Integer.parseInt(rs.getString("r_id"))));
      }
      return result;
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return null;
  }

  @Override
  public List<Attachment> getAttachmentsByUserForReimbursement(int u_id, int r_id) {
    List<Attachment> result = new ArrayList<>();
    try {
      String sql = "select * from attachments where u_id = ? and r_id = ? order by a_id";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, Integer.toString(u_id));
      ps.setString(2, Integer.toString(r_id));
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        result.add(new Attachment(Integer.parseInt(rs.getString("a_id")), rs.getString("title"), rs.getString("type"),
            rs.getString("src"), Integer.parseInt(rs.getString("u_id")), Integer.parseInt(rs.getString("r_id"))));

      }
      return result;
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return null;
  }

  @Override
  public Attachment getAttachment(int a_id) {
    try {
      String sql = "select * from attachments where a_id = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, Integer.toString(a_id));
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        return new Attachment(Integer.parseInt(rs.getString("a_id")), rs.getString("title"), rs.getString("type"),
            rs.getString("src"), Integer.parseInt(rs.getString("u_id")), Integer.parseInt(rs.getString("r_id")));
      }
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return null;
  }

  @Override
  public boolean createAttachment(Attachment attachment) {
    try {
      String sql = "call create_new_attachment(?, ?, ?, ?, ?)";
      CallableStatement cs = conn.prepareCall(sql);
      cs.setString(1, attachment.getTitle());
      cs.setString(2, attachment.getType());
      cs.setString(3, attachment.getSrc());
      cs.setString(4, Integer.toString(attachment.getU_id()));
      cs.setString(5, Integer.toString(attachment.getR_id()));
      cs.execute();
      return true;
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return false;
  }

  @Override
  public boolean deleteAttachment(int a_id) {
    try {
      String sql = "delete from attachments where a_id = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, Integer.toString(a_id));
      ps.execute();
      return true;
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return false;
  }

  @Override
  public boolean updateAttachment(Attachment attachment) {
    try {
      String sql = "update comments set title = ?, type = ?, src = ?, u_id = ?, r_id = ? where a_id = ?";
      CallableStatement cs = conn.prepareCall(sql);
      cs.setString(1, attachment.getTitle());
      cs.setString(2, attachment.getType());
      cs.setString(3, attachment.getSrc());
      cs.setString(4, Integer.toString(attachment.getU_id()));
      cs.setString(5, Integer.toString(attachment.getR_id()));
      cs.setString(6, Integer.toString(attachment.getA_id()));

      cs.execute();
      return true;
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return false;
  }

}