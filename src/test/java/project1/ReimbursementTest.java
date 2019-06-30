package project1;

// import static org.junit.Assert.assertFalse;
// import static org.junit.Assert.assertTrue;

// import java.sql.Date;
// import java.sql.Timestamp;
// import java.util.List;

// import org.junit.Test;

// import project1.model.Reimbursement;
// import project1.service.ReimbursementService;

/**
 * Unit test for simple App.
 */
public class ReimbursementTest {
  // @Test
  // public void reimbursementSanityTest() {
  //   assertTrue(true);
  // }

  // @Test
  // public void canCreateAReimbursement() {
  //   assertTrue(ReimbursementService.createReimbursement(new Reimbursement(0, "seminar", 0.7, "pending",
  //       new Timestamp(100000000L), new Date(100000000L), new Date(100000000L), new Timestamp(100000000L),
  //       "123 main street", "some thing", 100, 0, "A-F", "C", "idk lol", "pending_supervisor_approval", 11)));
  //   List<Reimbursement> allReimbursements = ReimbursementService.getReimbursements();
  //   ReimbursementService.deleteReimbursement(allReimbursements.get(allReimbursements.size() - 1).getR_id());
  // }

  // @Test
  // public void canDeleteAReimbursement() {
  //   ReimbursementService.createReimbursement(new Reimbursement(0, "seminar", 0.7, "pending", new Timestamp(100000000L),
  //       new Date(100000000L), new Date(100000000L), new Timestamp(100000000L), "123 main street", "some thing", 100, 0,
  //       "A-F", "C", "idk lol", "pending_supervisor_approval", 11));
  //   List<Reimbursement> allReimbursements = ReimbursementService.getReimbursements();
  //   assertTrue(ReimbursementService.deleteReimbursement(allReimbursements.get(allReimbursements.size() - 1).getR_id()));
  // }

  // @Test
  // public void canGetAllReimbursements() {
  //   ReimbursementService.createReimbursement(new Reimbursement(0, "seminar", 0.7, "pending", new Timestamp(100000000L),
  //       new Date(100000000L), new Date(100000000L), new Timestamp(100000000L), "123 main street", "some thing", 100, 0,
  //       "A-F", "C", "idk lol", "pending_supervisor_approval", 11));
  //   List<Reimbursement> allReimbursements = ReimbursementService.getReimbursements();
  //   assertTrue(allReimbursements.size() > 0);
  //   ReimbursementService.deleteReimbursement(allReimbursements.get(allReimbursements.size() - 1).getR_id());
  // }

  // @Test
  // public void canGetReimbursementById() {
  //   ReimbursementService.createReimbursement(new Reimbursement(0, "seminar", 0.7, "pending", new Timestamp(100000000L),
  //       new Date(100000000L), new Date(100000000L), new Timestamp(100000000L), "123 main street", "some thing", 100, 0,
  //       "A-F", "C", "idk lol", "pending_supervisor_approval", 11));
  //   List<Reimbursement> allReimbursements = ReimbursementService.getReimbursements();
  //   Reimbursement lastReimbursementAdded = allReimbursements.get(allReimbursements.size() - 1);
  //   Reimbursement reimbursement = ReimbursementService.getReimbursement(lastReimbursementAdded.getR_id());
  //   assertTrue(reimbursement.equals(lastReimbursementAdded));
  //   ReimbursementService.deleteReimbursement(lastReimbursementAdded.getR_id());
  // }

  // @Test
  // public void canGetReimbursementsForUser() {
  //   ReimbursementService.createReimbursement(new Reimbursement(0, "seminar", 0.7, "pending", new Timestamp(100000000L),
  //       new Date(100000000L), new Date(100000000L), new Timestamp(100000000L), "123 main street", "some thing", 100, 0,
  //       "A-F", "C", "idk lol", "pending_supervisor_approval", 10));
  //   ReimbursementService.createReimbursement(new Reimbursement(0, "seminar", 0.7, "pending", new Timestamp(100000000L),
  //       new Date(100000000L), new Date(100000000L), new Timestamp(100000000L), "123 main street", "some thing", 100, 0,
  //       "A-F", "C", "idk lol", "pending_supervisor_approval", 11));
  //   ReimbursementService.createReimbursement(new Reimbursement(0, "seminar", 0.7, "pending", new Timestamp(100000000L),
  //       new Date(100000000L), new Date(100000000L), new Timestamp(100000000L), "123 main street", "some thing", 100, 0,
  //       "A-F", "C", "idk lol", "pending_supervisor_approval", 11));
  //   List<Reimbursement> allReimbursements = ReimbursementService.getReimbursements();
  //   Reimbursement firstReimbursementAdded = allReimbursements.get(allReimbursements.size() - 3);
  //   Reimbursement middleReimbursementAdded = allReimbursements.get(allReimbursements.size() - 2);
  //   Reimbursement lastReimbursementAdded = allReimbursements.get(allReimbursements.size() - 1);
  //   List<Reimbursement> reimbursements = ReimbursementService.getReimbursementsByUser(11);
  //   assertTrue(reimbursements.contains(lastReimbursementAdded));
  //   assertTrue(reimbursements.contains(middleReimbursementAdded));
  //   assertFalse(reimbursements.contains(firstReimbursementAdded));
  //   ReimbursementService.deleteReimbursement(lastReimbursementAdded.getR_id());
  //   ReimbursementService.deleteReimbursement(middleReimbursementAdded.getR_id());
  //   ReimbursementService.deleteReimbursement(firstReimbursementAdded.getR_id());
  // }

  // @Test
  // public void canUpdateAReimbursement() {
  //   ReimbursementService.createReimbursement(new Reimbursement(0, "seminar", 0.7, "pending", new Timestamp(100000000L),
  //       new Date(100000000L), new Date(100000000L), new Timestamp(100000000L), "123 main street", "some thing", 100, 0,
  //       "A-F", "C", "idk lol", "pending_supervisor_approval", 11));
  //   List<Reimbursement> allReimbursements = ReimbursementService.getReimbursements();
  //   Reimbursement lastReimbursementAdded = allReimbursements.get(allReimbursements.size() - 1);
  //   Reimbursement reimbursement = ReimbursementService.getReimbursement(lastReimbursementAdded.getR_id());
  //   reimbursement.setAmount_granted(1000);
  //   assertTrue(ReimbursementService.updateReimbursement(reimbursement));
  //   reimbursement = ReimbursementService.getReimbursement(reimbursement.getR_id());
  //   assertTrue(reimbursement.getAmount_granted() == 1000);
  //   ReimbursementService.deleteReimbursement(lastReimbursementAdded.getR_id());
  // }
}