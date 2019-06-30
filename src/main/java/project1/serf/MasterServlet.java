package project1.serf;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MasterServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  public MasterServlet() {
    super();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    RequestHelper.Process(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    RequestHelper.Process(request, response);
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    RequestHelper.Process(request, response);
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    RequestHelper.Process(request, response);
  }
}