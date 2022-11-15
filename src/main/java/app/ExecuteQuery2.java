package app;

import app.utils.Query;
import com.google.gson.Gson;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExecuteQuery2 {

    //========================== CREATE ==========================
    public static <T> void createTable(Class<T> clz) {
        try {
            Statement statement = ConnectionController.connect().createStatement();
            String createTableQuery = createTableQueryBuilder(clz);
            statement.execute(createTableQuery);
            ConnectionController.disconnect();
        } catch (IllegalAccessException | SQLException exception) {
            System.out.println(exception);
        }
    }

    //=========================== READ ===========================
    public static <T> List<T> readAll(Class<T> clz) {

        List<T> results = null;

        try {
            Statement statement = ConnectionController.connect().createStatement();
            Query query = new Query.Builder().select("*").from(getTableName(clz)).build();
            ResultSet rs = statement.executeQuery(query.getQuery());
            results = getResults(rs, clz);
            ConnectionController.disconnect();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException | SQLException exception) {
            System.out.println(exception);
        }
        return results;
    }

    public static <T> T readById(int id, Class<T> clz) {

        T result = null;

        try {
            Statement statement = ConnectionController.connect().createStatement();
            Query query = new Query.Builder().select("*").from(getTableName(clz)).where("id", String.valueOf(id)).build();
            ResultSet rs = statement.executeQuery(query.getQuery());
            result = getResult(rs, clz);
            ConnectionController.disconnect();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException | SQLException exception) {
            System.out.println(exception);
        }

        return result;
    }

    public static <T> List<T> readByProperty(String property, String value, Class<T> clz) {

        List<T> results = null;

        try {
            Statement statement = ConnectionController.connect().createStatement();
            String table = clz.getSimpleName().toLowerCase();
            Query query = new Query.Builder().select("*").from(table).where(property, value).build();
            ResultSet rs = statement.executeQuery(query.getQuery());
            results = getResults(rs, clz);
        }
        catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException | SQLException exception) {
            System.out.println(exception);
        }
        return results;
    }

    //=========================== ADD ============================

    public static <T> void addItem(T item) {
        try {
            Statement statement = ConnectionController.connect().createStatement();
            String addItemQuery = addItemQueryBuilder(item);
            statement.execute(addItemQuery);
            ConnectionController.disconnect();
        } catch (IllegalAccessException | SQLException exception) {
            System.out.println(exception);
        }
    }

    public static <T> void addItems(ArrayList<T> items, Class<T> clz) {
        for (T item : items) {
            addItem(item);
        }
    }

    //========================= UPDATE ===========================
    public static <T> void updatePropertyById(int id, String property, String value, Class<T> clz) {
        try {
            Statement statement = ConnectionController.connect().createStatement();
            Query query = new Query.Builder().update(getTableName(clz)).set(property, value).where("id", String.valueOf(id)).build();
            statement.executeUpdate(query.getQuery());
            ConnectionController.disconnect();
        } catch (SQLException exception) {
            System.out.println(exception);
        }
    }

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
                if (i != properties.size() - 1) {
                    updatedProperties += properties.get(i) + " = " + values.get(i) + ", ";
                } else {
                    updatedProperties += properties.get(i) + " = " + values.get(i);
                }
            }

            System.out.println(String.format("UPDATE %s SET %s WHERE id = '%s';", table, updatedProperties, id));
            String query = String.format("UPDATE %s SET %s WHERE id = '%s';", table, updatedProperties, id);

            statement.executeUpdate(query);

            ConnectionController.disconnect();

        } catch (IllegalAccessException | SQLException exception) {
            System.out.println(exception);
        }
    }

    //========================= DELETE ===========================

    public static <T> void deleteItemByProperty(String property, String value, Class<T> clz) {
        try {
            Statement statement = ConnectionController.connect().createStatement();
            String deleteByPropertyQuery = deleteItemByPropertyQueryBuilder(clz, property, value);
            statement.execute(deleteByPropertyQuery);
            ConnectionController.disconnect();
        } catch (IllegalAccessException | SQLException | NoSuchFieldException exception) {
            System.out.println(exception);
        }

    }

    public static <T> void deleteTable(Class<T> clz) {
        try {
            Statement statement = ConnectionController.connect().createStatement();
            statement.execute("DROP TABLE " + clz.getSimpleName().toLowerCase() + ";");
            ConnectionController.disconnect();
        } catch (SQLException exception) {
            System.out.println(exception);
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

    private static <T> String createReadAllQueryBuilder(Class<T> clz) throws IllegalAccessException {
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

    private static <T> T getResult(ResultSet rs, Class<T> clz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        List<T> results = getResults(rs, clz);
        return (results.size() == 0)? null : results.get(0);
    }

    private static <T> List<T> getResults(ResultSet rs, Class<T> clz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {

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
        return results;
    }

    private static <T> String getTableName(Class<T> clz) {
        return clz.getSimpleName().toLowerCase();
    }
}