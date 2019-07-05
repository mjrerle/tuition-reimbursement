package project1.webservice;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;

import project1.model.Reimbursement;
import project1.service.ReimbursementService;
import project1.utils.ReimbursementLogger;

public class ReimbursementWebService {
  public static Logger logger = ReimbursementLogger.logger;

  public static void getReimbursements(HttpServletRequest request, HttpServletResponse response) {

    List<Reimbursement> reimbursements = ReimbursementService.getReimbursements();

    try {
      if (reimbursements.size() > 0) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(reimbursements);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().append(json).close();
      } else {
        response.sendError(404);
      }
    } catch (IOException e) {
      logger.warn(e.getMessage());
      e.printStackTrace();
    }
  }

  public static void getReimbursementsByUser(HttpServletRequest request, HttpServletResponse response) {
    List<Reimbursement> reimbursements = new ArrayList<>();
    String maybeUid = request.getParameter("u_id");
    if (maybeUid != null) {
      int u_id = Integer.parseInt(maybeUid);
      reimbursements = ReimbursementService.getReimbursementsByUser(u_id);
    }
    try {
      if (reimbursements.size() > 0) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(reimbursements);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().append(json).close();
      } else {
        response.sendError(404);
      }
    } catch (IOException e) {
      logger.warn(e.getMessage());
      e.printStackTrace();
    }
  }

  public static void getReimbursement(HttpServletRequest request, HttpServletResponse response) {
    Reimbursement reimbursement = null;
    String maybeRid = request.getParameter("r_id");
    if (maybeRid != null) {
      int r_id = Integer.parseInt(maybeRid);
      reimbursement = ReimbursementService.getReimbursement(r_id);
    }
    try {
      if (reimbursement != null) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(reimbursement);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().append(json).close();
      } else {
        response.sendError(404);
      }
    } catch (IOException e) {
      logger.warn(e.getMessage());
      e.printStackTrace();
    }
  }

  public static void createReimbursement(HttpServletRequest request, HttpServletResponse response) {
    Reimbursement reimbursement = null;
    boolean succeeded = false;

    String maybeEventType = request.getParameter("event_type");
    String maybePercentCoverage = request.getParameter("percent_coverage");
    String maybeStatus = request.getParameter("status");
    String maybeSubmissionDate = request.getParameter("submission_date");
    String maybeEventStartDate = request.getParameter("event_start_date");
    String maybeEventEndDate = request.getParameter("event_end_date");
    String maybeEventDailyStartTime = request.getParameter("event_daily_start_time");
    String maybeEventAddress = request.getParameter("event_address");
    String maybeEventDescription = request.getParameter("event_description");
    String maybeAmountRequested = request.getParameter("amount_requested");
    String maybeAmountGranted = request.getParameter("amount_granted");
    String maybeEventGradingFormat = request.getParameter("event_grading_format");
    String maybeEventPassingGrade = request.getParameter("event_passing_grade");
    String maybeJustificationComment = request.getParameter("justification_comment");
    String maybeStage = request.getParameter("stage");
    String maybeUid = request.getParameter("u_id");

    if ((maybeEventType != null) && (maybePercentCoverage != null) && (maybeStatus != null)
        && (maybeSubmissionDate != null) && (maybeEventStartDate != null) && (maybeEventEndDate != null)
        && (maybeEventDailyStartTime != null) && (maybeEventAddress != null) && (maybeEventDescription != null)
        && (maybeAmountRequested != null) && (maybeAmountGranted != null) && (maybeEventGradingFormat != null)
        && (maybeEventPassingGrade != null) && (maybeJustificationComment != null) && (maybeStage != null)
        && (maybeUid != null)) {
      reimbursement = new Reimbursement(0, maybeEventType, Double.parseDouble(maybePercentCoverage), maybeStatus,
          new Timestamp(Long.parseLong(maybeSubmissionDate)), new Date(Long.parseLong(maybeEventStartDate)),
          new Date(Long.parseLong(maybeEventEndDate)), maybeEventDailyStartTime, maybeEventAddress,
          maybeEventDescription, Double.parseDouble(maybeAmountRequested), Double.parseDouble(maybeAmountGranted),
          maybeEventGradingFormat, maybeEventPassingGrade, maybeJustificationComment, Integer.parseInt(maybeStage),
          Integer.parseInt(maybeUid));
      
      succeeded = ReimbursementService.createReimbursement(reimbursement);
    }
    try {
      if (succeeded) {
        // get the reimbursement that was just added... yuck
        List<Reimbursement> reimbursements = ReimbursementService.getReimbursementsByUser(Integer.parseInt(maybeUid));
        reimbursement = reimbursements.get(reimbursements.size() - 1);

        // spawn a timer thread to count down from 7 days(how much time the
        // approver has to approve the request)
        new Timer().schedule(new TimerTask() {
          @Override
          public void run() {
            List<Reimbursement> reimbursements = ReimbursementService
                .getReimbursementsByUser(Integer.parseInt(maybeUid));
            Reimbursement reimbursement = reimbursements.get(reimbursements.size() - 1);
            reimbursement.setStage(reimbursement.getStage() + 1);
            switch (reimbursement.getStage()) {
            case 1:
              reimbursement.setStatus("approval_by_supervisor");
              break;
            case 2:
              reimbursement.setStatus("approval_by_department_head");
              break;
            case 3:
              reimbursement.setStatus("approval_by_first_benco");
              break;
            case 4:
              reimbursement.setStatus("appoval_by_final_benco");
              break;
            }
            ReimbursementService.updateReimbursement(reimbursement);
            logger.warn("===REIMBURSEMENT AUTO APPROVED===");
            logger.warn("===REIMBURSEMENT IS NOW AT STAGE " + reimbursement.getStage() + "===");
            if (reimbursement.getStage() >= 4) {
              cancel();
            }
          }
        }, System.currentTimeMillis(), 7 * 24 * 60 * 60 * 1000);
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(reimbursement.getR_id());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().append(json).close();
      } else {
        response.sendError(403);
      }
    } catch (IOException e) {
      logger.warn(e.getMessage());
      e.printStackTrace();
    }
  }

  public static void deleteReimbursement(HttpServletRequest request, HttpServletResponse response) {
    boolean succeeded = false;
    String maybeRid = request.getParameter("r_id");
    if (maybeRid != null) {
      int r_id = Integer.parseInt(maybeRid);
      succeeded = ReimbursementService.deleteReimbursement(r_id);
    }
    try {
      if (succeeded) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(succeeded);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().append(json).close();
      } else {
        response.sendError(403);
      }
    } catch (IOException e) {
      logger.warn(e.getMessage());
      e.printStackTrace();
    }
  }

  public static void updateReimbursement(HttpServletRequest request, HttpServletResponse response) {
    Reimbursement reimbursement = null;
    boolean succeeded = false;

    String maybeRid = request.getParameter("r_id");

    if (maybeRid != null) {
      reimbursement = ReimbursementService.getReimbursement(Integer.parseInt(maybeRid));
      if (reimbursement != null) {
        String maybeEventType = request.getParameter("event_type");
        if (maybeEventType != null) {
          reimbursement.setEvent_type(maybeEventType);
        }
        String maybePercentCoverage = request.getParameter("percent_coverage");
        if (maybePercentCoverage != null) {
          reimbursement.setPercent_coverage(Double.parseDouble(maybePercentCoverage));
        }
        String maybeStatus = request.getParameter("status");
        if (maybeStatus != null) {
          reimbursement.setStatus(maybeStatus);
        }
        String maybeSubmissionDate = request.getParameter("submission_date");
        if (maybeSubmissionDate != null) {
          reimbursement.setSubmission_date(new Timestamp(Long.parseLong(maybeSubmissionDate)));
        }
        String maybeEventStartDate = request.getParameter("event_start_date");
        if (maybeEventStartDate != null) {
          reimbursement.setEvent_start_date(new Date(Long.parseLong(maybeEventStartDate)));
        }
        String maybeEventEndDate = request.getParameter("event_end_date");
        if (maybeEventEndDate != null) {
          reimbursement.setEvent_end_date(new Date(Long.parseLong(maybeEventEndDate)));
        }
        String maybeEventDailyStartTime = request.getParameter("event_daily_start_time");
        if (maybeEventDailyStartTime != null) {
          reimbursement.setEvent_daily_start_time(maybeEventDailyStartTime);
        }
        String maybeEventAddress = request.getParameter("event_address");
        if (maybeEventAddress != null) {
          reimbursement.setEvent_address(maybeEventAddress);
        }
        String maybeEventDescription = request.getParameter("event_description");
        if (maybeEventDescription != null) {
          reimbursement.setEvent_description(maybeEventDescription);
        }
        String maybeAmountRequested = request.getParameter("amount_requested");
        if (maybeAmountRequested != null) {
          reimbursement.setAmount_requested(Double.parseDouble(maybeAmountRequested));
        }
        String maybeAmountGranted = request.getParameter("amount_granted");
        if (maybeAmountGranted != null) {
          reimbursement.setAmount_granted(Double.parseDouble(maybeAmountGranted));
        }
        String maybeEventGradingFormat = request.getParameter("event_grading_format");
        if (maybeEventGradingFormat != null) {
          reimbursement.setEvent_grading_format(maybeEventGradingFormat);
        }
        String maybeEventPassingGrade = request.getParameter("event_passing_grade");
        if (maybeEventPassingGrade != null) {
          reimbursement.setEvent_passing_grade(maybeEventPassingGrade);
        }
        String maybeJustificationComment = request.getParameter("justification_comment");
        if (maybeJustificationComment != null) {
          reimbursement.setJustification_comment(maybeJustificationComment);
        }
        String maybeStage = request.getParameter("stage");
        if (maybeStage != null) {
          reimbursement.setStage(Integer.parseInt(maybeStage));
        }
        String maybeUid = request.getParameter("u_id");
        if (maybeUid != null) {
          reimbursement.setU_id(Integer.parseInt(maybeUid));
        }
      }

      succeeded = ReimbursementService.updateReimbursement(reimbursement);
    }

    try {
      if (succeeded) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(succeeded);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().append(json).close();
      } else {
        response.sendError(403);
      }
    } catch (IOException e) {
      logger.warn(e.getMessage());
      e.printStackTrace();
    }
  }

  public static void getRidOfLastInserted(HttpServletRequest request, HttpServletResponse response) {

    int r_id = ReimbursementService.getRidOfLastInserted();
    try {
      if (r_id >= 0) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(r_id);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().append(json).close();
      } else {
        response.sendError(404);
      }
    } catch (IOException e) {
      logger.warn(e.getMessage());
      e.printStackTrace();
    }
  }

}