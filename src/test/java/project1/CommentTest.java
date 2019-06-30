package project1;

// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertFalse;
// import static org.junit.Assert.assertTrue;

// import java.sql.Date;
// import java.sql.Timestamp;
// import java.util.List;

// import org.junit.Test;

// import project1.model.Comment;
// import project1.model.Reimbursement;
// import project1.service.CommentService;
// import project1.service.ReimbursementService;

/**
 * Unit test for simple App.
 */
public class CommentTest {
  // @Test
  // public void reimbursementSanityTest() {
  //   assertTrue(true);
  // }

  // @Test
  // public void canCreateAComment() {
  //   ReimbursementService.createReimbursement(new Reimbursement(0, "seminar", 0.7, "pending", new Timestamp(100000000L),
  //       new Date(100000000L), new Date(100000000L), new Timestamp(100000000L), "123 main street", "some thing", 100, 0,
  //       "A-F", "C", "idk lol", "pending_supervisor_approval", 11));
  //   List<Reimbursement> allReimbursements = ReimbursementService.getReimbursements();
  //   Reimbursement lastReimbursementAdded = allReimbursements.get(allReimbursements.size() - 1);
  //   assertTrue(CommentService
  //       .createComment(new Comment(0, "ur cool", "fr fr bro", "unread", 0, lastReimbursementAdded.getR_id())));
  //   List<Comment> allComments = CommentService.getComments();
  //   Comment lastCommentAdded = allComments.get(allComments.size() - 1);
  //   CommentService.deleteComment(lastCommentAdded.getC_id());
  //   ReimbursementService.deleteReimbursement(lastReimbursementAdded.getR_id());
  // }

  // @Test
  // public void canDeleteAComment() {
  //   ReimbursementService.createReimbursement(new Reimbursement(0, "seminar", 0.7, "pending", new Timestamp(100000000L),
  //       new Date(100000000L), new Date(100000000L), new Timestamp(100000000L), "123 main street", "some thing", 100, 0,
  //       "A-F", "C", "idk lol", "pending_supervisor_approval", 11));
  //   List<Reimbursement> allReimbursements = ReimbursementService.getReimbursements();
  //   Reimbursement lastReimbursementAdded = allReimbursements.get(allReimbursements.size() - 1);
  //   CommentService.createComment(new Comment(0, "ur cool", "fr fr bro", "unread", 0, lastReimbursementAdded.getR_id()));
  //   List<Comment> allComments = CommentService.getComments();
  //   Comment lastCommentAdded = allComments.get(allComments.size() - 1);
  //   assertTrue(CommentService.deleteComment(lastCommentAdded.getC_id()));
  //   ReimbursementService.deleteReimbursement(lastReimbursementAdded.getR_id());
  // }

  // @Test
  // public void canGetAllComments() {
  //   ReimbursementService.createReimbursement(new Reimbursement(0, "seminar", 0.7, "pending", new Timestamp(100000000L),
  //       new Date(100000000L), new Date(100000000L), new Timestamp(100000000L), "123 main street", "some thing", 100, 0,
  //       "A-F", "C", "idk lol", "pending_supervisor_approval", 11));
  //   List<Reimbursement> allReimbursements = ReimbursementService.getReimbursements();
  //   Reimbursement lastReimbursementAdded = allReimbursements.get(allReimbursements.size() - 1);
  //   CommentService.createComment(new Comment(0, "ur cool", "fr fr bro", "unread", 0, lastReimbursementAdded.getR_id()));
  //   List<Comment> allComments = CommentService.getComments();
  //   assertTrue(allComments.size() == 1);
  //   Comment lastCommentAdded = allComments.get(allComments.size() - 1);
  //   CommentService.deleteComment(lastCommentAdded.getC_id());
  //   ReimbursementService.deleteReimbursement(lastReimbursementAdded.getR_id());
  // }

  // @Test
  // public void canGetASingleComment() {
  //   ReimbursementService.createReimbursement(new Reimbursement(0, "seminar", 0.7, "pending", new Timestamp(100000000L),
  //       new Date(100000000L), new Date(100000000L), new Timestamp(100000000L), "123 main street", "some thing", 100, 0,
  //       "A-F", "C", "idk lol", "pending_supervisor_approval", 11));
  //   List<Reimbursement> allReimbursements = ReimbursementService.getReimbursements();
  //   Reimbursement lastReimbursementAdded = allReimbursements.get(allReimbursements.size() - 1);
  //   CommentService.createComment(new Comment(0, "ur cool", "fr fr bro", "unread", 0, lastReimbursementAdded.getR_id()));
  //   List<Comment> allComments = CommentService.getComments();
  //   Comment comment = CommentService.getComment(allComments.get(allComments.size() - 1).getC_id());
  //   Comment lastCommentAdded = allComments.get(allComments.size() - 1);
  //   assertEquals(lastCommentAdded, comment);
  //   CommentService.deleteComment(lastCommentAdded.getC_id());
  //   ReimbursementService.deleteReimbursement(lastReimbursementAdded.getR_id());
  // }

  // @Test
  // public void canGetCommentsForAUser() {
  //   ReimbursementService.createReimbursement(new Reimbursement(0, "seminar", 0.7, "pending", new Timestamp(100000000L),
  //       new Date(100000000L), new Date(100000000L), new Timestamp(100000000L), "123 main street", "some thing", 100, 0,
  //       "A-F", "C", "idk lol", "pending_supervisor_approval", 11));
  //   List<Reimbursement> allReimbursements = ReimbursementService.getReimbursements();
  //   Reimbursement lastReimbursementAdded = allReimbursements.get(allReimbursements.size() - 1);
  //   CommentService.createComment(new Comment(0, "ur cool", "fr fr bro", "unread", 10, lastReimbursementAdded.getR_id()));
  //   CommentService.createComment(new Comment(0, "ur cool", "fr fr bro", "unread", 11, lastReimbursementAdded.getR_id()));
  //   CommentService.createComment(new Comment(0, "ur cool", "fr fr bro", "unread", 11, lastReimbursementAdded.getR_id()));

  //   List<Comment> allComments = CommentService.getComments();
  //   List<Comment> comments = CommentService.getCommentsByUser(11);
  //   Comment firstCommentAdded = allComments.get(allComments.size() - 3);
  //   Comment middleCommentAdded = allComments.get(allComments.size() - 2);
  //   Comment lastCommentAdded = allComments.get(allComments.size() - 1);

  //   assertFalse(comments.contains(firstCommentAdded));
  //   assertEquals(middleCommentAdded, comments.get(0));
  //   assertEquals(lastCommentAdded, comments.get(1));

  //   CommentService.deleteComment(firstCommentAdded.getC_id());
  //   CommentService.deleteComment(middleCommentAdded.getC_id());
  //   CommentService.deleteComment(lastCommentAdded.getC_id());

  //   ReimbursementService.deleteReimbursement(lastReimbursementAdded.getR_id());
    
  // }
}