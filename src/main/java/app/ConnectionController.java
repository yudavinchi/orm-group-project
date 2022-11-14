package app;

import app.utils.File;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class ConnectionController {

    private static final String url = "src/main/java/configurations/sql config.json";
    private static final HashMap<String, String> configurations = File.readJson(url);
    private static Connection connection;

    public static Connection connect() {
        try {
            connection = DriverManager.getConnection(
                    configurations.get("url"),
                    configurations.get("user"),
                    configurations.get("password"));
        } catch (SQLException exception) {
            System.out.println(exception);
        }
        return connection;
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
        }
    }
}
