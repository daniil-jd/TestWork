package mimi.servlets;

import mimi.dao.CatDAO;
import mimi.dao.CatDaoImpl;
import mimi.model.Cat;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@WebServlet(urlPatterns = "/add")
@MultipartConfig
public class CatAddServlet extends HttpServlet {
    private  CatDAO catDAO;
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
        request.getServletContext().getRequestDispatcher("/jsp/catadd.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("submit") != null) {
            String name = request.getParameter("name");
            Part filePart = request.getPart("file");

            if (!name.isEmpty() && filePart.getSize() > 0 && filePart.getContentType().equals("image/jpeg")) {
                InputStream fileContent = filePart.getInputStream();
                byte[] photo = new byte[(int) filePart.getSize()];
                fileContent.read(photo);
                fileContent.close();

                catDAO.create(new Cat(name, photo, 0));
            }
        }
        doGet(request, response);
    }

    @Override
    public void destroy() {
        catDAO.close();
    }


}
