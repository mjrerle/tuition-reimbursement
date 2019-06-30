package project1.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;

import project1.model.Role;
import project1.service.RoleService;
import project1.utils.ReimbursementLogger;

public class RoleWebService {
  public static Logger logger = ReimbursementLogger.logger;
  public static void getRolesForUser(HttpServletRequest request, HttpServletResponse response) {
    
    List<Role> roles = new ArrayList<>();
    String maybeUid = request.getParameter("u_id");
    String maybeUsername = request.getParameter("username");
    if(maybeUid != null) {
      int u_id = Integer.parseInt(maybeUid);
      roles = RoleService.getRolesForUser(u_id);
    } else if(maybeUsername != null) {
      roles = RoleService.getRolesForUser(maybeUsername);
    }
    try {
      if(roles.size() > 0) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(roles);
        response.getWriter().append(json).close();
      } else {
        response.sendError(404);
      }
    } catch(IOException e) {
      logger.warn(e.getMessage());
      e.printStackTrace();
    }
  }
}