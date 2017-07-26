import com.sun.org.apache.regexp.internal.RE;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by kienmaingoc on 7/9/17.
 */
public class DatabaseProcess {

    public static void main(String[] args) {
//        Create a table in the database
//
//        Connection connection = null;
//        Statement statement = null;
//        try {
//            Class.forName("org.sqlite.JDBC");
//            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
//            statement = connection.createStatement();
//            String sql = "CREATE TABLE checkin_time (id INTEGER PRIMARY KEY, name VARCHAR, time VARCHAR)";
//            statement.execute(sql);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        listDatabase();
    }

    public static void updateDatabase(String name, String time) {
        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            statement = connection.createStatement();
            String sql = "INSERT INTO checkin_time (name, time) VALUES ('" + name + "', '" + time + "');";
            statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void listDatabase() {
        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM checkin_time");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + " " + resultSet.getString("name") + " checkin at " + resultSet.getString("time"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
