package mimi;


import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Runner {
    public static void main(String[] args) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUsername("sa");
        dataSource.setPassword("");
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:~/jt2");

        save(dataSource);

    }

    private static void create (DataSource dataSource) {
        byte[] photo = null;
        FileInputStream inputStream = null;
        try {
            File inputFile = new File("D:\\Program\\barsik.jpg");
            inputStream = new FileInputStream(inputFile);
            photo = new byte[(int) inputFile.length()];
            inputStream.read(photo);
            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        String SQL_CREATE = "INSERT INTO cat (name, image) VALUES (?,?)";

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = null;
            statement = connection.prepareStatement(SQL_CREATE);
            statement.setString(1, "cat1");
            statement.setBytes (2, photo);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void save (DataSource dataSource) {
        String SQL_SELECT = "SELECT * FROM cat WHERE name = ?";
        byte[] photo = null;
        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(SQL_SELECT);
            statement.setString(1, "cat1");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                photo = resultSet.getBytes("image");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream("D:\\Program\\im2.jpg");
            stream.write(photo);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
