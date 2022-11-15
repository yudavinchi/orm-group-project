package app.utils;

import app.ConnectionController;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcAutoGenKey {
    public static void main(String[] args) throws SQLException {

//        Statement statement = ConnectionController.connect().createStatement();

        String sqlGetLastID = "INSERT INTO dog(Name) VALUES(?)";
        String studentName = "Ramesh Fadatare";


        PreparedStatement statement = ConnectionController.connect().prepareStatement(sqlGetLastID, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, studentName);
        statement.executeUpdate();



        String url = "jdbc:mysql://localhost:3306/demo?useSSL=false";
        String user = "root";
        String password = "root";

        String sql = "INSERT INTO Students(Name) VALUES(?)";

    try{
     Connection con = DriverManager.getConnection(url, user, password);
     PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); {

        preparedStatement.setString(1, studentName);
        preparedStatement.executeUpdate();

        try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {

            if (resultSet.first()) {

                System.out.printf("The ID of new student : %d", resultSet.getLong(1));
            }
        }

    } }
    catch (SQLException ex) {

        Logger lgr = Logger.getLogger(JdbcAutoGenKey.class.getName());
        lgr.log(Level.SEVERE, ex.getMessage(), ex);
    }
}
    }
