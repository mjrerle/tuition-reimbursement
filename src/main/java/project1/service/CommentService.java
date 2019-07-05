package project1.service;

import java.util.List;

import project1.dao.CommentDAO;
import project1.model.Comment;

public class CommentService {
  public static CommentDAO cdao = new CommentDAO();
  public static List<Comment> getComments() {
    return cdao.getComments();
  }

  public static List<Comment> getCommentsByUserForReimbursement(int u_id, int r_id) {
    return cdao.getCommentsByUserForReimbursement(u_id, r_id);
  }

  public static List<Comment> getCommentsForReimbursement(int r_id) {
    return cdao.getCommentsForReimbursement(r_id);
  }

  public static Comment getComment(int c_id) {
    return cdao.getComment(c_id);
  }

  public static boolean createComment(Comment comment) {
    return cdao.createComment(comment);
  }

  public static boolean deleteComment(int c_id) {
    return cdao.deleteComment(c_id);
  }

  public static boolean updateComment(Comment comment) {
    return cdao.updateComment(comment);
  }
}