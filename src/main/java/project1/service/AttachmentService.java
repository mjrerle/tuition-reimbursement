package project1.service;

import java.util.List;

import project1.dao.AttachmentDAO;
import project1.model.Attachment;;

public class AttachmentService {
  public static AttachmentDAO adao = new AttachmentDAO();
  public static List<Attachment> getAttachments() {
    return adao.getAttachments();
  }

  public static List<Attachment> getAttachmentsByUserForReimbursement(int u_id, int r_id) {
    return adao.getAttachmentsByUserForReimbursement(u_id, r_id);
  }

  public static Attachment getAttachment(int a_id) {
    return adao.getAttachment(a_id);
  }

  public static boolean createAttachment(Attachment attachment) {
    return adao.createAttachment(attachment);
  }

  public static boolean deleteAttachment(int a_id) {
    return adao.deleteAttachment(a_id);
  }

  public static boolean updateAttachment(Attachment attachment) {
    return adao.updateAttachment(attachment);
  }
}