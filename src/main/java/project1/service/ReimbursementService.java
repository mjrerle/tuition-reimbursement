package project1.service;

import java.util.List;

import project1.dao.ReimbursementDAO;
import project1.model.Reimbursement;

public class ReimbursementService {
  public static ReimbursementDAO rdao = new ReimbursementDAO();
  public static List<Reimbursement> getReimbursements() {
    return rdao.getReimbursements();
  }

  public static List<Reimbursement> getReimbursementsByUser(int u_id) {
    return rdao.getReimbursementsByUser(u_id);
  }

  public static Reimbursement getReimbursement(int r_id) {
    return rdao.getReimbursement(r_id);
  }

  public static boolean createReimbursement(Reimbursement reimbursement) {
    return rdao.createReimbursement(reimbursement);
  }

  public static boolean deleteReimbursement(int r_id) {
    return rdao.deleteReimbursement(r_id);
  }

  public static boolean updateReimbursement(Reimbursement reimbursement) {
    return rdao.updateReimbursement(reimbursement);
  }

  public static int getRidOfLastInserted() {
    return rdao.getRidOfLastInserted();
  }
}