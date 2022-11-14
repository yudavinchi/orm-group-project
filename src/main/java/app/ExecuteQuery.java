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

    public static <T> void addOne(T item, Class<T> clz) throws FileNotFoundException {
        try {

            Statement statement = ConnectionController.connect().createStatement();

            List<String> columns = new ArrayList<>();
            List<String> values = new ArrayList<>();
            Field[] declaredFields = clz.getDeclaredFields();

            for (Field field : declaredFields
            ) {
                columns.add(field.getName());
                field.setAccessible(true);
                values.add("'" + field.get(item) + "'");
            }

            String tableName = clz.getSimpleName().toLowerCase();


            System.out.println(String.format(new Query.Builder().insertInto(tableName).withFields(columns).andValues(values).build().getQuery()));
            System.out.println(String.format("INSERT INTO %s (%s) VALUES (%s);", clz.getSimpleName().toLowerCase(), String.join(",", columns), String.join(",", values)));
            statement.execute(String.format("INSERT INTO %s (%s) VALUES (%s);", clz.getSimpleName().toLowerCase(), String.join(",", columns), String.join(",", values)));

            ConnectionController.disconnect();
        } catch (SQLException e) {
            System.out.println(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    ///////////////////////////////////////////////////////////////////////////////// IN PROCESS
    public static <T> void addItem(T item, Class<T> clz) throws FileNotFoundException {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            List<String> columns = new ArrayList<>();
            List<String> values = new ArrayList<>();
            Field[] declaredFields = clz.getDeclaredFields();

            for (Field field : declaredFields
            ) {
                columns.add(field.getName());
                field.setAccessible(true);
                values.add("'" + field.get(item.toString()) + "'");
            }
            System.out.println("col: " + columns);
            System.out.println("values: " + values);
            System.out.println(String.format("INSERT INTO %s (%s) VALUES (%s);", clz.getSimpleName().toLowerCase(), String.join(",", columns), String.join(",", values)));
            statement.execute(String.format("INSERT INTO %s (%s) VALUES (%s);", clz.getSimpleName().toLowerCase(), String.join(",", columns), String.join(",", values)));
        } catch (SQLException e) {
            System.out.println(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //----------------------------------------------------------- SQL CLEAN FUNCTIONS
    public static <T> void createTable(Class<T> clz) throws SQLException, FileNotFoundException {
        String str = "";
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        try {
            Field[] declaredFields = clz.getDeclaredFields();
            for (Field field : declaredFields)
                str += field.getName() + " " + returnTypeString(field.getType().toString()) + " NOT NULL, ";
            str = str.substring(0, str.length() - 2);

            statement.execute(String.format("CREATE TABLE %s (%s);", clz.getSimpleName().toLowerCase(), str));
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static <T> void deleteItem(T item) throws IllegalAccessException, SQLException, FileNotFoundException {
        Connection connection = getConnection();
        String strQuery = "DELETE FROM " + item.getClass().getSimpleName().toLowerCase() + " WHERE ";
        Field[] declaredFields = item.getClass().getDeclaredFields();
        for (Field field : declaredFields)
            strQuery += returnTypeStringToDel(field, item);
        strQuery = strQuery.substring(0, strQuery.length() - 5);
        strQuery += ";";
        Statement statement = connection.createStatement();
        statement.execute(strQuery);
        connection.close();
    }

    //----------------------------------------------------------- HELP FUNCTIONS

    private static Connection getConnection() throws SQLException, FileNotFoundException {
        HashMap<String, String> configurations = File.readJson("src/main/java/configurations/sql config.json");

        Connection connection = DriverManager.getConnection(
                configurations.get("url"),
                configurations.get("user"),
                configurations.get("password"));
        return connection;
    }

    private static <T> String objectToJsonString(Object o) {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    private static String returnTypeString(String type) {
        switch (type) {
            case "class java.lang.Boolean":
                return "BIT(1)";
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
            case "class java.util.Date":
            case "class java.time.LocalDateTime":
                return "DATETIME";
            case "char":
                return "VARCHAR(1)";
            default:
                return "TEXT(500)";
        }
    }

    private static <T> String returnTypeStringToDel(Field field, T item) throws IllegalAccessException {
        String stringType = returnTypeString(field.getType().toString());
        switch (stringType) {
            case "BIT(1)":
            case "BINARY":
            case "SMALLINT":
            case "INTEGER":
            case "BIGINT":
            case "DOUBLE":
            case "FLOAT":
                return field.getName() + " = " + field.get(item) + " AND ";
            default:
                return field.getName() + " = '" + field.get(item) + "'" + " AND ";
        }
    }

    public static <T> List<T> readAll(Class<T> clz) {
        try {
            Statement statement = ConnectionController.connect().createStatement();

            ResultSet rs = statement.executeQuery(String.format("select * from %s", clz.getSimpleName().toLowerCase()));
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

    public static <T> T readById(int id, Class<T> clz) {
        try {
            Statement statement = ConnectionController.connect().createStatement();

            ResultSet rs = statement.executeQuery(String.format("select * from %s where id = %s", clz.getSimpleName().toLowerCase(), id));
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
}

