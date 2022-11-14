package app;

import app.utils.File;
import app.utils.Vehicle;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;

public class Client {
    public static void main(String args[]) throws Exception {

        HashMap<String, String> configurations = File.readJson("src/main/java/configurations/sql config.json");


        User user = new User(123, "David", "Yudin");
        Vehicle vehicle = new Vehicle("abc", "zxc", 1, 2022, "no comment");

        ExecuteQuery.createTable(Dog.class);
    }

//    public static <T> T executeQueryCreateTable(Class<T> clz) throws FileNotFoundException, SQLException {
//        HashMap<String, String> configurations = File.readJson("src/main/java/configurations/sql config.json");
//
//        Connection connection = DriverManager.getConnection(
//                configurations.get("url"),
//                configurations.get("user"),
//                configurations.get("password"));
//
//        Statement statement = connection.createStatement();
//        List<String> columns = new ArrayList<>();
//        Field[] declaredFields = clz.getDeclaredFields();
//        List<String> declaredTypes = new ArrayList<>();
//
//        for (Field classField : declaredFields) {
//            System.out.println(classField.getType().toString());
//            declaredTypes.add(classField.getType().toString());
//        }
//
//        for (Field field : declaredFields) {
//            columns.add(field.getName());
//            field.setAccessible(true);
//        }
//
//        Map<String, String> map = new HashMap<>();
//
//        map.put("int", "INTEGER");
//        map.put("class java.lang.String", "VARCHAR(255)");
//
////        String sql = "CREATE TABLE REGISTRATION " +
////                "(id INTEGER not NULL, " +
////                " first VARCHAR(255), " +
////                " last VARCHAR(255), " +
////                " age INTEGER, " +
////                " PRIMARY KEY ( id ))";
//
//        StringBuilder temp = new StringBuilder();
//
//        for (int i = 0; i < columns.size(); i++) {
//            if (i != columns.size() - 1) {
//                temp.append(declaredFields[i].getName()).append(" ").append(map.get(declaredTypes.get(i))).append(", ");
//            } else {
//                temp.append(declaredFields[i].getName()).append(" ").append(map.get(declaredTypes.get(i)));
//            }
//        }
//
//        System.out.println(String.format("CREATE TABLE %s (%s);", clz.getSimpleName().toLowerCase(), temp));
//        statement.execute(String.format("CREATE TABLE %s (%s);", clz.getSimpleName().toLowerCase(), temp));
//
//        return null;
//    }
//
//    public static <T> T executeQueryAddOne(T item, Class<T> clz) throws FileNotFoundException {
//        try {
//
//            HashMap<String, String> configurations = File.readJson("src/main/java/configurations/sql config.json");
//
//            Connection connection = DriverManager.getConnection(
//                    configurations.get("url"),
//                    configurations.get("user"),
//                    configurations.get("password"));
//
//            Statement statement = connection.createStatement();
//            List<String> columns = new ArrayList<>();
//            List<String> values = new ArrayList<>();
//            Field[] declaredFields = clz.getDeclaredFields();
//
//            for (Field field : declaredFields
//            ) {
//                columns.add(field.getName());
//                field.setAccessible(true);
//                values.add("'" + field.get(item) + "'");
//            }
//
//            System.out.println("col: " + columns);
//            System.out.println("values: " + values);
//            System.out.println(String.format("INSERT INTO %s (%s) VALUES (%s);", clz.getSimpleName().toLowerCase(), String.join(",", columns), String.join(",", values)));
//            statement.execute(String.format("INSERT INTO %s (%s) VALUES (%s);", clz.getSimpleName().toLowerCase(), String.join(",", columns), String.join(",", values)));
//        } catch (SQLException e) {
//            System.out.println(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        return null;
//    }

}
