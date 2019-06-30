package project1.dao;

import java.util.List;

import project1.model.Attachment;

public interface IAttachment {
  public List<Attachment> getAttachments();
  public List<Attachment> getAttachmentsByUserForReimbursement(int u_id, int r_id);
  public Attachment getAttachment(int c_id);
  public boolean createAttachment(Attachment attachment);
  public boolean deleteAttachment(int c_id);
  public boolean updateAttachment(Attachment attachment);
}
