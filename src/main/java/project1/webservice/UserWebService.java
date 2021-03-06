package project1.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;

import project1.model.User;
import project1.service.UserService;
import project1.utils.ReimbursementLogger;

public class UserWebService {
  public static Logger logger = ReimbursementLogger.logger;


  public static void getUsers(HttpServletRequest request, HttpServletResponse response) {
    List<User> users = UserService.getUsers();
    try {
      if (users.size() > 0 ) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(users);
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

  public static void getUser(HttpServletRequest request, HttpServletResponse response) {
    String maybeUid = request.getParameter("u_id");
    String maybeUsername = request.getParameter("username");
    String maybePassword = request.getParameter("password");
    User u = null;
    if (maybeUid != null) {
      int u_id = Integer.parseInt(maybeUid);
      u = UserService.getUser(u_id);
    } else if (maybeUsername != null && maybePassword != null) {
      u = UserService.getUser(maybeUsername, maybePassword);
    } else if (maybeUsername != null) {
      u = UserService.getUser(maybeUsername);
    }
    try {
      if (u != null) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(u);
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

  public static void getUserSupervisor(HttpServletRequest request, HttpServletResponse response) {
    String maybeUid = request.getParameter("u_id");
    User u = null;
    if (maybeUid != null) {
      int u_id = Integer.parseInt(maybeUid);
      u = UserService.getUserSupervisor(u_id);
    }
    try {
      if (u != null) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(u);
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

  public static void getUserDepartmentHead(HttpServletRequest request, HttpServletResponse response) {
    String maybeUid = request.getParameter("u_id");

    User u = null;
    if (maybeUid != null) {
      int u_id = Integer.parseInt(maybeUid);
      u = UserService.getUserDepartmentHead(u_id);
    }
    try {
      if (u != null) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(u);
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

  public static void getUnderlings(HttpServletRequest request, HttpServletResponse response) {
    String maybeUid = request.getParameter("u_id");

    List<User> users = new ArrayList<>();
    if (maybeUid != null) {
      int u_id = Integer.parseInt(maybeUid);
      users = UserService.getUnderlings(u_id);
    }
    try {
      if (users.size() > 0) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(users);
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

  public static void login(HttpServletRequest request, HttpServletResponse response) {
    String maybeUsername = request.getParameter("username");
    String maybePassword = request.getParameter("password");
    boolean succeeded = false;
    User u = null;
    if (maybeUsername != null && maybePassword != null) {
      u = UserService.getUser(maybeUsername, maybePassword);
      if (u != null) {
        succeeded = true;
        // HttpSession sess = request.getSession();
        // sess.setAttribute("loggedIn", succeeded);
        // sess.setAttribute("loggedInUsername", u.getUsername());
        // sess.setAttribute("loggedInUid", u.getU_id());
        // sess.setMaxInactiveInterval(300);
        Cookie loggedIn = new Cookie("loggedIn", Boolean.toString(succeeded));
        loggedIn.setPath("/");
        Cookie loggedInUser = new Cookie("loggedInUsername", u.getUsername());
        loggedInUser.setPath("/");
        Cookie loggedInUid = new Cookie("loggedInUid", Integer.toString(u.getU_id()));
        loggedInUid.setPath("/");
        loggedIn.setMaxAge(600);
        loggedInUser.setMaxAge(600);
        loggedInUid.setMaxAge(600);
        response.addCookie(loggedIn);
        response.addCookie(loggedInUser);
        response.addCookie(loggedInUid);
      }
    }
    try {
      if (u != null && succeeded) {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(u);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().append(json).close();
        // response.sendRedirect("/project1/home/dashboard.html");
      } else {
        response.sendRedirect("/project1/login.html");
      }
    } catch (IOException e) {
      logger.warn(e.getMessage());
      e.printStackTrace();
    }
  }

  public static void logout(HttpServletRequest request, HttpServletResponse response) {

    HttpSession sess = request.getSession();
    sess.invalidate();

    try {
      response.sendRedirect("/project1/login.html");
    } catch (IOException e) {
      logger.warn(e.getMessage());
      e.printStackTrace();
    }
  }
}