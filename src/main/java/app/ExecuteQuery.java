package app;

import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ExecuteQuery {

    private static final String url = "src/main/java/configurations/sql config.json";
    private static final Connection connection = Connect.to("src/main/java/configurations/sql config.json");

    public static <T> void createTable(Class<T> clz) throws SQLException {

        Statement statement = connection.createStatement();

        List<String> columns = new ArrayList<>();
        Field[] declaredFields = clz.getDeclaredFields();
        List<String> declaredTypes = new ArrayList<>();

        for (Field classField : declaredFields) {
            declaredTypes.add(classField.getType().toString());
        }

        for (Field field : declaredFields) {
            columns.add(field.getName());
            field.setAccessible(true);
        }

        Map<String, String> map = new HashMap<>();

        map.put("int", "INTEGER");
        map.put("class java.lang.String", "VARCHAR(255)");

        StringBuilder temp = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            if (i != columns.size() - 1) {
                temp.append(declaredFields[i].getName()).append(" ").append(map.get(declaredTypes.get(i))).append(", ");
            } else {
                temp.append(declaredFields[i].getName()).append(" ").append(map.get(declaredTypes.get(i)));
            }
        }
        statement.execute(String.format("CREATE TABLE %s (%s);", clz.getSimpleName().toLowerCase(), temp));
    }


    public static <T> void addOne(T item, Class<T> clz) throws FileNotFoundException {
        try {

            Statement statement = connection.createStatement();

            List<String> columns = new ArrayList<>();
            List<String> values = new ArrayList<>();
            Field[] declaredFields = clz.getDeclaredFields();

            for (Field field : declaredFields
            ) {
                columns.add(field.getName());
                field.setAccessible(true);
                values.add("'" + field.get(item) + "'");
            }

            System.out.println("col: " + columns);
            System.out.println("values: " + values);
            System.out.println(String.format("INSERT INTO %s (%s) VALUES (%s);", clz.getSimpleName().toLowerCase(), String.join(",", columns), String.join(",", values)));
            statement.execute(String.format("INSERT INTO %s (%s) VALUES (%s);", clz.getSimpleName().toLowerCase(), String.join(",", columns), String.join(",", values)));
        } catch (SQLException e) {
            System.out.println(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> readAll(Class<T> clz) {
        try {
            Statement statement = connection.createStatement();

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
            connection.close();
            return results;

        } catch (Exception e) {
            System.out.println("general exception " + e);
        }
        return null;
    }

    public static <T> T readById(int id,Class<T> clz) {
        try {
            Statement statement = connection.createStatement();

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
            connection.close();
            return result;

        } catch (Exception e) {
            System.out.println("general exception " + e);
        }
        return null;
    }
}
