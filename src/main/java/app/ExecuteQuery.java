package app;

import app.utils.File;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecuteQuery {

    public static <T> void createTable(Class<T> clz) throws FileNotFoundException, SQLException {
        HashMap<String, String> configurations = File.readJson("src/main/java/configurations/sql config.json");

        Connection connection = DriverManager.getConnection(
                configurations.get("url"),
                configurations.get("user"),
                configurations.get("password"));

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

            HashMap<String, String> configurations = File.readJson("src/main/java/configurations/sql config.json");

            Connection connection = DriverManager.getConnection(
                    configurations.get("url"),
                    configurations.get("user"),
                    configurations.get("password"));

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
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
