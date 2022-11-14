package app;

import app.utils.File;

import java.sql.*;
import java.util.HashMap;

public class Client {
    public static void main(String args[]) throws Exception {
        try {

            HashMap<String, String> configurations = File.readJson("src/main/java/configurations/sql config.json");

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    configurations.get("url"), configurations.get("user"), configurations.get("password"));

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from random_table");
            while (rs.next())
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
