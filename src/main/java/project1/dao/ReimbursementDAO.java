package project1.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import project1.model.Reimbursement;
import project1.utils.DBConnector;
import project1.utils.ReimbursementLogger;

public class ReimbursementDAO implements IReimbursement {
  public static Logger logger = ReimbursementLogger.logger;
  public static Connection conn = DBConnector.getConnection();

  @Override
  public List<Reimbursement> getReimbursements() {
    List<Reimbursement> result = new ArrayList<>();
    try {
      String sql = "select * from reimbursements order by r_id";
      PreparedStatement ps = conn.prepareStatement(sql);

      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        result.add(new Reimbursement(Integer.parseInt(rs.getString("r_id")), rs.getString("event_type"),
            Double.parseDouble(rs.getString("percent_coverage")), rs.getString("status"),
            rs.getTimestamp("submission_date"), rs.getDate("event_start_date"), rs.getDate("event_end_date"),
            rs.getTimestamp("event_daily_start_time"), rs.getString("event_address"), rs.getString("event_description"),
            Double.parseDouble(rs.getString("amount_requested")), Double.parseDouble(rs.getString("amount_granted")),
            rs.getString("event_grading_format"), rs.getString("event_passing_grade"),
            rs.getString("justification_comment"), rs.getString("stage"), Integer.parseInt(rs.getString("u_id"))));
      }
      return result;
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return null;
  }

  @Override
  public List<Reimbursement> getReimbursementsByUser(int u_id) {
    List<Reimbursement> result = new ArrayList<>();
    try {
      String sql = "select * from reimbursements where u_id = ? order by r_id";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, Integer.toString(u_id));
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        result.add(new Reimbursement(Integer.parseInt(rs.getString("r_id")), rs.getString("event_type"),
            Double.parseDouble(rs.getString("percent_coverage")), rs.getString("status"),
            rs.getTimestamp("submission_date"), rs.getDate("event_start_date"), rs.getDate("event_end_date"),
            rs.getTimestamp("event_daily_start_time"), rs.getString("event_address"), rs.getString("event_description"),
            Double.parseDouble(rs.getString("amount_requested")), Double.parseDouble(rs.getString("amount_granted")),
            rs.getString("event_grading_format"), rs.getString("event_passing_grade"),
            rs.getString("justification_comment"), rs.getString("stage"), Integer.parseInt(rs.getString("u_id"))));

      }
      return result;
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return null;
  }

  @Override
  public Reimbursement getReimbursement(int r_id) {
    try {
      String sql = "select * from reimbursements where r_id = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, Integer.toString(r_id));
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        return new Reimbursement(Integer.parseInt(rs.getString("r_id")), rs.getString("event_type"),
            Double.parseDouble(rs.getString("percent_coverage")), rs.getString("status"),
            rs.getTimestamp("submission_date"), rs.getDate("event_start_date"), rs.getDate("event_end_date"),
            rs.getTimestamp("event_daily_start_time"), rs.getString("event_address"), rs.getString("event_description"),
            Double.parseDouble(rs.getString("amount_requested")), Double.parseDouble(rs.getString("amount_granted")),
            rs.getString("event_grading_format"), rs.getString("event_passing_grade"),
            rs.getString("justification_comment"), rs.getString("stage"), Integer.parseInt(rs.getString("u_id")));
      }
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return null;
  }

  @Override
  public boolean createReimbursement(Reimbursement reimbursement) {
    try {
      String sql = "call create_new_reimbursement(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      CallableStatement cs = conn.prepareCall(sql);
      cs.setString(1, reimbursement.getEvent_type());
      cs.setString(2, Double.toString(reimbursement.getPercent_coverage()));
      cs.setString(3, reimbursement.getStatus());
      cs.setTimestamp(4, reimbursement.getSubmission_date());
      cs.setDate(5, reimbursement.getEvent_start_date());
      cs.setDate(6, reimbursement.getEvent_end_date());
      cs.setTimestamp(7, reimbursement.getEvent_daily_start_time());
      cs.setString(8, reimbursement.getEvent_address());
      cs.setString(9, reimbursement.getEvent_description());
      cs.setString(10, Double.toString(reimbursement.getAmount_requested()));
      cs.setString(11, Double.toString(reimbursement.getAmount_granted()));
      cs.setString(12, reimbursement.getEvent_grading_format());
      cs.setString(13, reimbursement.getEvent_passing_grade());
      cs.setString(14, reimbursement.getJustification_comment());
      cs.setString(15, reimbursement.getStage());
      cs.setString(16, Integer.toString(reimbursement.getU_id()));

      cs.execute();

      return true;

    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return false;
  }

  @Override
  public boolean deleteReimbursement(int r_id) {
    try {
      String sql = "delete from reimbursements where r_id = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, Integer.toString(r_id));
      ps.execute();
      return true;
    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }

    return false;
  }

  @Override
  public boolean updateReimbursement(Reimbursement reimbursement) {
    try {
      String sql = "update reimbursements set event_type = ?, percent_coverage = ?, status = ?, submission_date = ?, event_start_date = ?, event_end_date = ?, event_daily_start_time = ?, event_address = ?, event_description = ?, amount_requested = ?, amount_granted = ?, event_grading_format = ?, event_passing_grade = ?, justification_comment = ?, stage = ?, u_id = ? where r_id = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, reimbursement.getEvent_type());
      ps.setString(2, Double.toString(reimbursement.getPercent_coverage()));
      ps.setString(3, reimbursement.getStatus());
      ps.setTimestamp(4, reimbursement.getSubmission_date());
      ps.setDate(5, reimbursement.getEvent_start_date());
      ps.setDate(6, reimbursement.getEvent_end_date());
      ps.setTimestamp(7, reimbursement.getEvent_daily_start_time());
      ps.setString(8, reimbursement.getEvent_address());
      ps.setString(9, reimbursement.getEvent_description());
      ps.setString(10, Double.toString(reimbursement.getAmount_requested()));
      ps.setString(11, Double.toString(reimbursement.getAmount_granted()));
      ps.setString(12, reimbursement.getEvent_grading_format());
      ps.setString(13, reimbursement.getEvent_passing_grade());
      ps.setString(14, reimbursement.getJustification_comment());
      ps.setString(15, reimbursement.getStage());
      ps.setString(16, Integer.toString(reimbursement.getU_id()));
      ps.setString(17, Integer.toString(reimbursement.getR_id()));

      ps.execute();
      return true;

    } catch (SQLException e) {
      logger.warn(e.getMessage());
    }
    return false;
  }

}