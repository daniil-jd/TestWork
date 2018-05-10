package mimi.dao;

import mimi.model.Cat;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class CatDaoImpl implements CatDAO {
    private Connection connection;

    //language=SQL
    private final String SQL_CREATE = "INSERT INTO cat (name, image, rating) VALUES (?,?,?)";
    //language=SQL
    private final String SQL_SELECT_ALL = "SELECT * FROM cat";
    //language=SQL
    private final String SQL_SELECT = "SELECT * FROM cat WHERE name = ?";
    //language=SQL
    private final String SQL_UPDATE = "UPDATE cat SET rating = ? WHERE name = ?";

    public CatDaoImpl (DataSource dataSource) {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(Cat cat) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(SQL_CREATE);
            statement.setString(1, cat.getName());
            statement.setBytes(2, cat.getBytes());
            statement.setInt(3, cat.getRating());
            statement.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Cat> getAllCats() {
        try {
            List<Cat> cats = new ArrayList<>();
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                byte[] image = resultSet.getBytes("image");
                int rating = resultSet.getInt("rating");

                Cat cat = new Cat(name, image, rating);

                cats.add(cat);
            }
            return cats;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<Cat> getCatByName(String name) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                byte[] photo = resultSet.getBytes("image");
                int rating = resultSet.getInt("rating");
                return Optional.of(new Cat(name, photo, rating));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void update(Cat cat) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(SQL_UPDATE);
            statement.setInt(1, cat.getRating());
            statement.setString(2, cat.getName());
            statement.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
