package mimi.servlets;

import mimi.dao.CatDAO;
import mimi.dao.CatDaoImpl;
import mimi.model.Cat;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;


@WebServlet(urlPatterns = "/counting")
public class CountingServlet extends HttpServlet {
    private CatDAO catDAO;

    @Override
    public void init() throws ServletException {
        Properties prop = new Properties();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        try {
            prop.load(new FileInputStream(getServletContext().getRealPath("/WEB-INF/classes/db.properties")));
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Cat> cats = catDAO.getAllCats();
        Collections.sort(cats);

        request.setAttribute("catList", cats);
        request.setAttribute("image1", cats.get(0).getPhoto());
        request.getRequestDispatcher("/jsp/counting2.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    public void destroy() {
        catDAO.close();
    }


}
