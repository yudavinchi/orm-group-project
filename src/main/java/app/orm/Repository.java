package app.orm;

import java.util.ArrayList;
import java.util.List;

/*
    Example:
    Repository<User> repository = new Repository(User.class);
    User user = repository.findOne(3);
*/

public class Repository<T> {
    public Class<T> clz;

    public Repository(Class clz) {
        this.clz = clz;
    }

    //========================== CREATE ==========================
    public void createTable() {
        ExecuteQuery.createTable(clz);
    }

    //=========================== READ ===========================
    public List<T> readAll() {
        return ExecuteQuery.readAll(clz);
    }

    public T readById(int id) {

        return ExecuteQuery.readById(id, clz);
    }

    public List<T> readByProperty(String property, String value) {
        return ExecuteQuery.readByProperty(property, value, clz);
    }

    //=========================== ADD ============================
    public void addItem(T item) {
        ExecuteQuery.addItem(item);
    }

    public void addItems(List<T> items) {
        ExecuteQuery.addItems((ArrayList<T>) items, clz);
    }

    //========================= UPDATE ===========================
    public void updatePropertyById(int id, String property, String value) {
        ExecuteQuery.updatePropertyById(id, property, value, clz);
    }

    public void updateItemById(int id, T item) {
        ExecuteQuery.updateEntireItemById(id, item, clz);
    }

    //========================= DELETE ===========================
    public void deleteItemByProperty(String property, String value) {
        ExecuteQuery.deleteItemByProperty(property, value, clz);
    }

    public void deleteTable() {
        ExecuteQuery.deleteTable(clz);
    }
}
