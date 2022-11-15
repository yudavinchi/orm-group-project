package app.orm;

import app.orm.utils.File;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

class ConnectionController {

    private static final String url = "src/main/java/configurations/sql config.json";
    private static final HashMap<String, String> configurations = File.readJson(url);
    private static Connection connection;

    private static Logger logger = LogManager.getLogger(ConnectionController.class.getName());

    public static Connection connect() {
        try {
            connection = DriverManager.getConnection(
                    configurations.get("url"),
                    configurations.get("user"),
                    configurations.get("password"));
        } catch (SQLException exception) {
            logger.error(exception);
            System.out.println(exception);
        }
        return connection;
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException exception) {
            logger.error(exception);
            System.out.println(exception);
        }
    }
}
