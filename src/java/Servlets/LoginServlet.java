package Servlets;

import Models.User;
import Services.CookieUtil;
import Services.UserService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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
        if(request.getParameter("Logout") != null)
        {
            request.setAttribute("message", "You logged out... Good job!");
            HttpSession session = request.getSession();
            session.invalidate();
        }
        
        if(request.getSession().getAttribute("user") != null)
        {
            response.sendRedirect("Home");
        }
        else
        {
            Cookie[] cookies = request.getCookies();
            String userName = CookieUtil.getCookieValue(cookies, "username");
            if(userName != null || !userName.equals(""))
            {
                request.setAttribute("userName", userName);
            }
            getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
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
        else
        {
             HttpSession session = request.getSession();
             
             
             session.setAttribute("user", user);
             if(request.getParameter("remember") != null)
             {
                Cookie c = new Cookie("username", username);
                c.setMaxAge(30 * 60);
                c.setPath("/");
                response.addCookie(c);
             }
             else 
             {
                 Cookie[] cookies = request.getCookies();
                 for(Cookie cookie : cookies)
                 {
                     cookie.setMaxAge(0);
                     cookie.setPath("/");
                     response.addCookie(cookie);
                 }
                 
             }

             response.sendRedirect("Home");
        }
    }

}
