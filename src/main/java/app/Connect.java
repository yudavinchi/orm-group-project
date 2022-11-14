package app;

import app.utils.File;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class Connect {
    static Connection connection;

    public static Connection to(String url) {
        try {
            // reads sql config json file
            HashMap<String, String> configurations = File.readJson(url);

            connection = DriverManager.getConnection(
                    configurations.get("url"),
                    configurations.get("user"),
                    configurations.get("password"));
        } catch (FileNotFoundException | SQLException exception) {
            System.out.println(exception);
        }
        return connection;
    }
}
