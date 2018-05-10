package mimi.dao;

import mimi.model.Cat;

import java.util.List;
import java.util.Optional;

public interface CatDAO {
    void create (Cat cat);
    List<Cat> getAllCats();
    Optional<Cat> getCatByName (String name);
    void update (Cat cat);
    void close();

}
