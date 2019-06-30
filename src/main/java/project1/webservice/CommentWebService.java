package project1.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;

import project1.model.Comment;
import project1.service.CommentService;
import project1.utils.ReimbursementLogger;

public class CommentWebService {
  public static Logger logger = ReimbursementLogger.logger;

  public static void getComments(HttpServletRequest request, HttpServletResponse response) {

    List<Comment> comments = CommentService.getComments();

    try {
      if (comments.size() > 0) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(comments);
        response.getWriter().append(json).close();
      } else {
        response.sendError(404);
      }
    } catch (IOException e) {
      logger.warn(e.getMessage());
      e.printStackTrace();
    }
  }

  public static void getCommentsByUserForReimbursement(HttpServletRequest request, HttpServletResponse response) {
    List<Comment> comments = new ArrayList<>();
    String maybeUid = request.getParameter("u_id");
    String maybeRid = request.getParameter("r_id");

    if (maybeUid != null && maybeRid != null) {
      int u_id = Integer.parseInt(maybeUid);
      int r_id = Integer.parseInt(maybeRid);
      comments = CommentService.getCommentsByUserForReimbursement(u_id, r_id);
    }

    try {
      if (comments.size() > 0) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(comments);
        response.getWriter().append(json).close();
      } else {
        response.sendError(404);
      }
    } catch (IOException e) {
      logger.warn(e.getMessage());
      e.printStackTrace();
    }
  }

  public static void getComment(HttpServletRequest request, HttpServletResponse response) {
    Comment comment = null;
    String maybeCid = request.getParameter("c_id");

    if (maybeCid != null) {
      int c_id = Integer.parseInt(maybeCid);
      comment = CommentService.getComment(c_id);
    }

    try {
      if (comment != null) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(comment);
        response.getWriter().append(json).close();
      } else {
        response.sendError(403);
      }
    } catch (IOException e) {
      logger.warn(e.getMessage());
      e.printStackTrace();
    }
  }

  public static void createComment(HttpServletRequest request, HttpServletResponse response) {
    boolean succeeded = false;
    String maybeTitle = request.getParameter("title");
    String maybeBody = request.getParameter("body");
    String maybeStatus = request.getParameter("status");
    String maybeUid = request.getParameter("u_id");
    String maybeRid = request.getParameter("r_id");
    if ((maybeTitle != null) && (maybeBody != null) && (maybeStatus != null) && (maybeUid != null)
        && (maybeRid != null)) {
      succeeded = CommentService.createComment(
          new Comment(0, maybeTitle, maybeBody, maybeStatus, Integer.parseInt(maybeUid), Integer.parseInt(maybeRid)));
    }

    try {
      if (succeeded) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(succeeded);
        response.getWriter().append(json).close();
      } else {
        response.sendError(403);
      }
    } catch (IOException e) {
      logger.warn(e.getMessage());
      e.printStackTrace();
    }
  }

  public static void deleteComment(HttpServletRequest request, HttpServletResponse response) {
    boolean succeeded = false;
    String maybeCid = request.getParameter("c_id");

    if (maybeCid != null) {
      int c_id = Integer.parseInt(maybeCid);
      succeeded = CommentService.deleteComment(c_id);
    }

    try {
      if (succeeded) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(succeeded);
        response.getWriter().append(json).close();
      } else {
        response.sendError(403);
      }
    } catch (IOException e) {
      logger.warn(e.getMessage());
      e.printStackTrace();
    }
  }

  public static void updateComment(HttpServletRequest request, HttpServletResponse response) {
    boolean succeeded = false;
    Comment comment = null;
    String maybeCid = request.getParameter("c_id");
    if (maybeCid != null) {
      int c_id = Integer.parseInt(maybeCid);
      comment = CommentService.getComment(c_id);
      if(comment != null) {
        String maybeTitle = request.getParameter("title");
        if(maybeTitle != null) {
          comment.setTitle(maybeTitle);
        }
        String maybeBody = request.getParameter("body");
        if(maybeBody != null) {
          comment.setBody(maybeBody);
        }
        String maybeStatus = request.getParameter("status");
        if(maybeStatus != null) {
          comment.setStatus(maybeStatus);
        }
        String maybeUid = request.getParameter("u_id");
        if(maybeUid != null) {
          comment.setU_id(Integer.parseInt(maybeUid));
        }
        String maybeRid = request.getParameter("r_id");
        if(maybeRid != null) {
          comment.setR_id(Integer.parseInt(maybeRid));
        }
      }
    }

    try {
      if (succeeded) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(succeeded);
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