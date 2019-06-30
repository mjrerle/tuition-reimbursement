package project1.dao;

import java.util.List;

import project1.model.Reimbursement;

public interface IReimbursement {
  public List<Reimbursement> getReimbursements();
  public List<Reimbursement> getReimbursementsByUser(int u_id);
  public Reimbursement getReimbursement(int r_id);
  public boolean createReimbursement(Reimbursement reimbursement);
  public boolean deleteReimbursement(int r_id);
  public boolean updateReimbursement(Reimbursement reimbursement);
}