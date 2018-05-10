package mimi.servlets;

import mimi.dao.CatDAO;
import mimi.dao.CatDaoImpl;
import mimi.model.Cat;
import org.apache.commons.codec.binary.Base64;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;


@WebServlet(urlPatterns = "/vote")
public class VoteServlet extends HttpServlet {
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
        HttpSession session = request.getSession(true);
        CopyOnWriteArrayList randomList;
        boolean ft= false;
        Cat cat1 = null, cat2 = null;
        if (session.getAttribute("vote") == null){
            randomList = setRandomList();
            session.setAttribute("size", randomList.size());

            ft = true;
            session.setAttribute("vote", randomList);
        }
        else {
            randomList = (CopyOnWriteArrayList)session.getAttribute("vote");
        }

        if (randomList.size() >= 2 && ((session.getAttribute("fromPost")) != null || ft)) {
            session.setAttribute("stage", (Integer.parseInt(session.getAttribute("size").toString()) - randomList.size()) / 2 + "/" + Integer.parseInt(session.getAttribute("size").toString()) / 2);
//            cat1 = getRandomCat(randomList);
//            cat2 = getRandomCat(randomList);
//            session.setAttribute("vote", randomList);
            session.removeAttribute("fromPost");
            session.setAttribute("cat1", getRandomCat(randomList));
            session.setAttribute("cat2", getRandomCat(randomList));
            session.setAttribute("vote", randomList);
        }
        cat1 = (Cat) session.getAttribute("cat1");
        cat2 = (Cat) session.getAttribute("cat2");
        request.setAttribute("image1", cat1.getPhoto());
        request.setAttribute("image2", cat2.getPhoto());
        session.setAttribute("lcat1", cat1.getName());
        session.setAttribute("lcat2", cat2.getName());

        request.getServletContext().getRequestDispatcher("/jsp/vote.jsp").forward(request, response);

//        else {
//            response.sendRedirect(getServletContext().getRealPath("/counting"));
//        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Optional<Cat> cat = Optional.empty();
        if (request.getParameter("bcat1") != null) {
            cat = catDAO.getCatByName(session.getAttribute("lcat1").toString());
        }
        else
        if (request.getParameter("bcat2") != null) {
            cat = catDAO.getCatByName(session.getAttribute("lcat2").toString());
        }
        cat.get().setRating();
        catDAO.update(cat.get());
        session.removeAttribute("lcat1");
        session.removeAttribute("lcat2");

        if (((CopyOnWriteArrayList)session.getAttribute("vote")).size() >= 2){
            session.setAttribute("fromPost", true);
            response.sendRedirect(request.getContextPath() + "/vote");
        }
        else {
            session.setAttribute("voted", true);
            response.sendRedirect(request.getContextPath() + "/counting");
        }
    }

    private CopyOnWriteArrayList setRandomList() {
        return new CopyOnWriteArrayList<Cat>(catDAO.getAllCats());
    }
    private Cat getRandomCat(CopyOnWriteArrayList list){
        int random_num = (int) (Math.random() * list.size());
        Cat cat = (Cat) list.remove(random_num);
        return cat;
    }

    @Override
    public void destroy() {
        catDAO.close();
    }


}
