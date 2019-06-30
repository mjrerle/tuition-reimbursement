package project1.dao;

import java.util.List;

import project1.model.Comment;

public interface IComment {
  public List<Comment> getComments();
  public List<Comment> getCommentsByUserForReimbursement(int u_id, int r_id);
  public Comment getComment(int c_id);
  public boolean createComment(Comment comment);
  public boolean deleteComment(int c_id);
  public boolean updateComment(Comment comment);
}
