package app;

import app.utils.File;
import app.utils.Query;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class ExecuteQuery {

    private static final String url = "src/main/java/configurations/sql config.json";

    /*
        Repository<User> repository = new Repository(User.class);
        User user = repository.findOne(3);
     */

    // Create table
    public static <T> void createTable(Class<T> clz) throws SQLException, IllegalAccessException {
        Statement statement = ConnectionController.connect().createStatement();
        String createTableQuery = createTableQueryBuilder(clz);
        statement.execute(createTableQuery);
        ConnectionController.disconnect();
    }

    // Add item
    public static <T> void addItem(T item) throws SQLException, IllegalAccessException {
        Statement statement = ConnectionController.connect().createStatement();
        String addItemQuery = addItemQueryBuilder(item);
        statement.execute(addItemQuery);
        ConnectionController.disconnect();
    }

    // Delete Item by property
    public static <T> void deleteItemByProperty(Class<T> clz, String property, String value) throws
            IllegalAccessException, SQLException, FileNotFoundException, NoSuchFieldException {
        Statement statement = ConnectionController.connect().createStatement();
        String deleteByPropertyQuery = deleteItemByPropertyQueryBuilder(clz, property, value);
        statement.execute(deleteByPropertyQuery);
        ConnectionController.disconnect();
    }

    // Delete table
    public static <T> void deleteTable(Class<T> clz) throws SQLException, FileNotFoundException {
        Statement statement = ConnectionController.connect().createStatement();
        statement.execute("DROP TABLE " + clz.getSimpleName().toLowerCase() + ";");
        ConnectionController.disconnect();
    }

    // Add items
    public static <T> void addItems(ArrayList<T> items, Class<T> clz) {
        for (T item : items) {
            try {
                addItem(item);
            } catch (IllegalAccessException | SQLException exception) {
                System.out.println(exception);
            }
        }
    }

    //--------------------------------------------------------------- QUERY STRING BUILDERS
    private static <T> String addItemQueryBuilder(T item) throws IllegalAccessException {
        List<String> columns = new ArrayList<>();
        List<String> values = new ArrayList<>();
        Field[] declaredFields = item.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            columns.add(field.getName());
            field.setAccessible(true);
            if (needToConvertToJson(returnValueTypeString(field.getType().toString()))) {
                values.add("'" + objectToJsonString(field.get(item)) + "'");
            } else {
                values.add("'" + field.get(item) + "'");
            }
        }
        return String.format("INSERT INTO %s (%s) VALUES (%s);", item.getClass().getSimpleName().toLowerCase(), String.join(",", columns), String.join(",", values));
    }

    private static <T> String createTableQueryBuilder(Class<T> clz) throws IllegalAccessException {
        String strQuery = "";
        Field[] declaredFields = clz.getDeclaredFields();
        for (Field field : declaredFields)
            strQuery += field.getName() + " " + returnValueTypeString(field.getType().toString()) + " NOT NULL, ";
        strQuery = strQuery.substring(0, strQuery.length() - 2);
        return String.format("CREATE TABLE %s (%s);", clz.getSimpleName().toLowerCase(), strQuery);
    }


    private static <T> String deleteItemByPropertyQueryBuilder(Class<T> clz, String property, String value) throws
            NoSuchFieldException, IllegalAccessException {
        String strQuery = "DELETE FROM " + clz.getSimpleName().toLowerCase() + " WHERE ";
        Field[] declaredFields = clz.getDeclaredFields();
        Boolean foundProperty = false;
        for (Field field : declaredFields) {
            if (field.getName().equals(property)) {
                foundProperty = true;
                strQuery += " " + field.getName();
            }
        }
        strQuery += " = " + "'" + value + "';";
        if (!foundProperty) {
            throw new NoSuchFieldException("The input property doesn't exists in the table");
        }
        return strQuery;
    }

    //----------------------------------------------------------- HELP FUNCTIONS

    private static String returnValueTypeString(String type) {
        switch (type) {
            case "class java.lang.Boolean":
                return "VARCHAR(5)";
            case "class java.lang.Byte":
                return "BINARY";
            case "class java.lang.Short":
                return "SMALLINT";
            case "int":
                return "INTEGER";
            case "class java.lang.String":
                return "VARCHAR(256)";
            case "long":
                return "BIGINT";
            case "float":
                return "FLOAT";
            case "double":
                return "DOUBLE";
            case "class java.time.LocalDateTime":
                return "DATETIME";
            case "char":
                return "VARCHAR(1)";
            default:
                return "TEXT(500)";
        }
    }

    private static <T> Boolean needToConvertToJson(String stringType) {
        if (stringType.equals("TEXT(500)")) {
            return true;
        }
        return false;
    }

    private static <T> String objectToJsonString(Object o) {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    //------------------------------------------------------------------------------------------------------------------------

    // Read all items in table
    public static <T> List<T> readAll(Class<T> clz) {
        try {
            Statement statement = ConnectionController.connect().createStatement();

            String table = clz.getSimpleName().toLowerCase();

            Query query = new Query.Builder().select("*").from(table).build();
            ResultSet rs = statement.executeQuery(query.getQuery());

            List<T> results = new ArrayList<>();

            while (rs.next()) {
                Constructor<T> constructor = clz.getConstructor();
                T item = (T) constructor.newInstance();
                Field[] declaredFields = clz.getDeclaredFields();

                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    field.set(item, rs.getObject(field.getName()));
                }
                results.add(item);
            }
            ConnectionController.disconnect();
            return results;

        } catch (Exception e) {
            System.out.println("general exception " + e);
        }
        return null;
    }

    // Read item by ID
    public static <T> T readById(int id, Class<T> clz) {
        try {
            Statement statement = ConnectionController.connect().createStatement();

            String table = clz.getSimpleName().toLowerCase();

            Query query = new Query.Builder().select("*").from(table).where("id", String.valueOf(id)).build();
            ResultSet rs = statement.executeQuery(query.getQuery());

            T result = null;

            while (rs.next()) {
                Constructor<T> constructor = clz.getConstructor();
                T item = (T) constructor.newInstance();
                Field[] declaredFields = clz.getDeclaredFields();

                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    field.set(item, rs.getObject(field.getName()));
                }
                result = item;
            }
            ConnectionController.disconnect();
            return result;

        } catch (Exception e) {
            System.out.println("general exception " + e);
        }
        return null;
    }

    // Read by property
    public static <T> List<T> readByProperty(String property, String value, Class<T> clz) {
        try {
//            Statement statement = ConnectionController.connect().createStatement();
//            System.out.println(String.format("SELECT * FROM %s WHERE %s='%s';", clz.getSimpleName().toLowerCase(), property, value));
//            ResultSet rs = statement.executeQuery(String.format("SELECT * FROM %s WHERE %s='%s';", clz.getSimpleName().toLowerCase(), property, value));


//
//            List<T> results = new ArrayList<>();
//
//            while (rs.next()) {
//                Constructor<T> constructor = clz.getConstructor();
//                T item = (T) constructor.newInstance();
//                Field[] declaredFields = clz.getDeclaredFields();
//
//                for (Field field : declaredFields) {
//                    field.setAccessible(true);
//                    field.set(item, rs.getObject(field.getName()));
//                }
//                results.add(item);
//            }
//            ConnectionController.disconnect();
//            return results;
            Statement statement = ConnectionController.connect().createStatement();

            String table = clz.getSimpleName().toLowerCase();

            Query query = new Query.Builder().select("*").from(table).where(property, value).build();
            ResultSet rs = statement.executeQuery(query.getQuery());

            List<T> results = new ArrayList<>();

            while (rs.next()) {
                Constructor<T> constructor = clz.getConstructor();
                T item = (T) constructor.newInstance();
                Field[] declaredFields = clz.getDeclaredFields();

                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    field.set(item, rs.getObject(field.getName()));
                }
                results.add(item);
            }
            ConnectionController.disconnect();
            return results;

        } catch (Exception e) {
            System.out.println("general exception " + e);
        }
        return null;
    }

    // Update property by ID
    public static <T> void updatePropertyById(int id, String property, String value, Class<T> clz) {
        try {
            Statement statement = ConnectionController.connect().createStatement();

            /*
            UPDATE table_name
            SET column1 = value1, column2 = value2, ...
            WHERE condition;
             */
            String table = clz.getSimpleName().toLowerCase();

            Query query = new Query.Builder().update(table).set(property, value).where("id", String.valueOf(id)).build();
            statement.executeUpdate(query.getQuery());

            ConnectionController.disconnect();

        } catch (Exception e) {
            System.out.println("general exception " + e);
        }
    }

    // Update entire item by ID
    public static <T> void updateEntireItemById(int id, T item, Class<T> clz) {
        try {
            Statement statement = ConnectionController.connect().createStatement();

            List<String> properties = new ArrayList<>();
            List<String> values = new ArrayList<>();
            Field[] declaredFields = clz.getDeclaredFields();

            for (Field field : declaredFields) {

                properties.add(field.getName());
                field.setAccessible(true);
                values.add("'" + field.get(item) + "'");
            }

            String table = clz.getSimpleName().toLowerCase();
            String updatedProperties = "";

            for (int i = 0; i < properties.size(); i++) {
                if(i != properties.size() -1) {
                    updatedProperties += properties.get(i) + " = " + values.get(i) + ", ";
                }
                else{
                    updatedProperties += properties.get(i) + " = " + values.get(i);
                }
            }

            System.out.println(String.format("UPDATE %s SET %s WHERE id = '%s';", table, updatedProperties, id));
            String query = String.format("UPDATE %s SET %s WHERE id = '%s';", table, updatedProperties, id);

            statement.executeUpdate(query);

            ConnectionController.disconnect();

        } catch (Exception e) {
            System.out.println("general exception " + e);
        }
    }
}

