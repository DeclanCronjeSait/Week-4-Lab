package Servlets;

import Models.User;
import Services.UserService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author 687333
 */
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
            
        getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if(username.equals(null) || username.equals("") ||
                password.equals(null) || password.equals(""))
        {
            request.setAttribute("message", "please enter in both a username and a password");
            getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            return;
        }
        
        UserService userService = new UserService();
        User user = userService.login(username, password);
        
        if(user == null)
        {
            request.setAttribute("message", "Invalid username or password... Please try again.");
            getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
        else{
             HttpSession session = request.getSession();
             session.setAttribute("user", user);
             response.sendRedirect("Home");
        }
    }

}
