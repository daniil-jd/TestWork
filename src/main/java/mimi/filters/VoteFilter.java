package mimi.filters;

import mimi.dao.CatDAO;
import mimi.dao.CatDaoImpl;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;


@WebFilter(urlPatterns = "/vote")
public class VoteFilter implements Filter {
    private CatDAO catDAO;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Properties prop = new Properties();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        try {
            prop.load(new FileInputStream(filterConfig.getServletContext().getRealPath("/WEB-INF/classes/db.properties")));
            String dbUrl = prop.getProperty("db.url");
            String dbUsername = prop.getProperty("db.username");
            String dbPassword = prop.getProperty("");
            String driverClassName = prop.getProperty("db.driverClassName");

            dataSource.setUsername(dbUsername);
            dataSource.setPassword(dbPassword);
            dataSource.setUrl(dbUrl);
            dataSource.setDriverClassName(driverClassName);

            catDAO = new CatDaoImpl(dataSource);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("voted") != null) {
            //servletRequest.getServletContext().getRequestDispatcher(request.getContextPath() + "/counting").forward(request, response);
            response.sendRedirect(request.getContextPath() + "/counting");
        }
        else
        if (session != null && session.getAttribute("size") != null &&((int)session.getAttribute("size")) < catDAO.getAllCats().size()) {
            session.removeAttribute("vote");
            filterChain.doFilter(request, response);
        }
        else
            filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        catDAO.close();
    }
}
