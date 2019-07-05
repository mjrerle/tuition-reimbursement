package project1.serf;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import project1.utils.ReimbursementLogger;
import project1.webservice.AttachmentWebService;
import project1.webservice.CommentWebService;
import project1.webservice.ReimbursementWebService;
import project1.webservice.RoleWebService;
import project1.webservice.UserWebService;

public class RequestHelper {
  public static Logger logger = ReimbursementLogger.logger;

  static final String baseURI = "/project1";

  static final String apiBase = baseURI + "/api";

  static final String userApi = "/user";
  static final String roleApi = "/role";
  static final String reimbursementApi = "/reimbursement";
  static final String commentApi = "/comment";
  static final String attachmentApi = "/attachment";

  public static void Process(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String uri = request.getRequestURI();
    logger.info(uri);
    switch (uri) {

    case apiBase + userApi + "/getUser.do": {
      UserWebService.getUser(request, response);
      break;
    }

    case apiBase + userApi + "/getUsers.do": {
      UserWebService.getUsers(request, response);
      break;
    }

    case apiBase + userApi + "/getUserSupervisor.do": {
      UserWebService.getUserSupervisor(request, response);
      break;
    }

    case apiBase + userApi + "/getUserDepartmentHead.do": {
      UserWebService.getUserDepartmentHead(request, response);
      break;
    }

    case apiBase + userApi + "/getUnderlings.do": {
      UserWebService.getUnderlings(request, response);
      break;
    }

    case apiBase + userApi + "/login.do": {
      UserWebService.login(request, response);
      break;
    }

    case apiBase + userApi + "/logout.do": {
      UserWebService.logout(request, response);
      break;
    }

    case apiBase + roleApi + "/getRolesForUser.do": {
      RoleWebService.getRolesForUser(request, response);
      break;
    }

    case apiBase + reimbursementApi + "/getReimbursement.do": {
      ReimbursementWebService.getReimbursement(request, response);
      break;
    }

    case apiBase + reimbursementApi + "/getRidOfLastInserted.do": {
      ReimbursementWebService.getRidOfLastInserted(request, response);
      break;
    }

    case apiBase + reimbursementApi + "/getReimbursements.do": {
      ReimbursementWebService.getReimbursements(request, response);
      break;
    }

    case apiBase + reimbursementApi + "/getReimbursementsByUser.do": {
      ReimbursementWebService.getReimbursementsByUser(request, response);
      break;
    }

    case apiBase + reimbursementApi + "/createReimbursement.do": {
      ReimbursementWebService.createReimbursement(request, response);
      break;
    }

    case apiBase + reimbursementApi + "/updateReimbursement.do": {
      ReimbursementWebService.updateReimbursement(request, response);
      break;
    }

    case apiBase + reimbursementApi + "/deleteReimbursement.do": {
      ReimbursementWebService.deleteReimbursement(request, response);
      break;
    }

    case apiBase + commentApi + "/getComments.do": {
      CommentWebService.getComments(request, response);
      break;
    }

    case apiBase + commentApi + "/getComment.do": {
      CommentWebService.getComment(request, response);
      break;
    }

    case apiBase + commentApi + "/getCommentsForReimbursement.do": {
      CommentWebService.getCommentsForReimbursement(request, response);
      break;
    }

    case apiBase + commentApi + "/getCommentsByUserForReimbursement.do": {
      CommentWebService.getCommentsByUserForReimbursement(request, response);
      break;
    }

    case apiBase + commentApi + "/createComment.do": {
      CommentWebService.createComment(request, response);
      break;
    }

    case apiBase + commentApi + "/updateComment.do": {
      CommentWebService.updateComment(request, response);
      break;
    }

    case apiBase + commentApi + "/deleteComment.do": {
      CommentWebService.deleteComment(request, response);
      break;
    }

    case apiBase + attachmentApi + "/getAttachments.do": {
      AttachmentWebService.getAttachments(request, response);
      break;
    }

    case apiBase + attachmentApi + "/getAttachment.do": {
      AttachmentWebService.getAttachment(request, response);
      break;
    }

    case apiBase + attachmentApi + "/getAttachmentsByUserForReimbursement.do": {
      AttachmentWebService.getAttachmentsByUserForReimbursement(request, response);
      break;
    }

    case apiBase + attachmentApi + "/createAttachment.do": {
      AttachmentWebService.createAttachment(request, response);
      break;
    }

    case apiBase + attachmentApi + "/updateAttachment.do": {
      AttachmentWebService.updateAttachment(request, response);
      break;
    }

    case apiBase + attachmentApi + "/deleteAttachment.do": {
      AttachmentWebService.deleteAttachment(request, response);
      break;
    }

    }
  }
}