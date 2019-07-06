package project1.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;

import project1.model.Attachment;
import project1.service.AttachmentService;
import project1.utils.ReimbursementLogger;

public class AttachmentWebService {
  public static Logger logger = ReimbursementLogger.logger;

  public static void getAttachments(HttpServletRequest request, HttpServletResponse response) {

    List<Attachment> attachments = AttachmentService.getAttachments();

    try {
      if (attachments.size() > 0) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(attachments);
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

  public static void getAttachmentsByUserForReimbursement(HttpServletRequest request, HttpServletResponse response) {
    List<Attachment> attachments = new ArrayList<>();
    String maybeUid = request.getParameter("u_id");
    String maybeRid = request.getParameter("r_id");

    if (maybeUid != null && maybeRid != null) {
      int u_id = Integer.parseInt(maybeUid);
      int r_id = Integer.parseInt(maybeRid);
      attachments = AttachmentService.getAttachmentsByUserForReimbursement(u_id, r_id);
    }

    try {
      ObjectMapper om = new ObjectMapper();
      String json = om.writeValueAsString(attachments);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().append(json).close();
    } catch (IOException e) {
      logger.warn(e.getMessage());
      e.printStackTrace();
    }
  }

  public static void getAttachment(HttpServletRequest request, HttpServletResponse response) {
    Attachment attachment = null;
    String maybeAid = request.getParameter("a_id");

    if (maybeAid != null) {
      int a_id = Integer.parseInt(maybeAid);
      attachment = AttachmentService.getAttachment(a_id);
    }

    try {
      if (attachment != null) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(attachment);
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

  public static void createAttachment(HttpServletRequest request, HttpServletResponse response) {
    boolean succeeded = false;
    String maybeTitle = request.getParameter("title");
    String maybeType = request.getParameter("type");
    String maybeSrc = request.getParameter("src");
    String maybeUid = request.getParameter("u_id");
    String maybeRid = request.getParameter("r_id");
    if ((maybeTitle != null) && (maybeType != null) && (maybeSrc != null) && (maybeUid != null) && (maybeRid != null)) {
      succeeded = AttachmentService.createAttachment(
          new Attachment(0, maybeTitle, maybeType, maybeSrc, Integer.parseInt(maybeUid), Integer.parseInt(maybeRid)));
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

  public static void deleteAttachment(HttpServletRequest request, HttpServletResponse response) {
    boolean succeeded = false;
    String maybeAid = request.getParameter("a_id");

    if (maybeAid != null) {
      int a_id = Integer.parseInt(maybeAid);
      succeeded = AttachmentService.deleteAttachment(a_id);
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

  public static void updateAttachment(HttpServletRequest request, HttpServletResponse response) {
    boolean succeeded = false;
    Attachment attachment = null;
    String maybeAid = request.getParameter("a_id");
    if (maybeAid != null) {
      int a_id = Integer.parseInt(maybeAid);
      attachment = AttachmentService.getAttachment(a_id);
      if (attachment != null) {
        String maybeTitle = request.getParameter("title");
        if (maybeTitle != null) {
          attachment.setTitle(maybeTitle);
        }
        String maybeType = request.getParameter("type");
        if (maybeType != null) {
          attachment.setType(maybeType);
        }
        String maybeSrc = request.getParameter("src");
        if (maybeSrc != null) {
          attachment.setSrc(maybeSrc);
        }
        String maybeUid = request.getParameter("u_id");
        if (maybeUid != null) {
          attachment.setU_id(Integer.parseInt(maybeUid));
        }
        String maybeRid = request.getParameter("r_id");
        if (maybeRid != null) {
          attachment.setR_id(Integer.parseInt(maybeRid));
        }
      }
    }

    try {
      if (succeeded) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(succeeded);
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