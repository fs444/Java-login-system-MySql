package loginpackage;


import java.sql.Connection;
import java.sql.DriverManager;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author david
 */
public class MySQLConnection {
    private static String database = "java_registration_form";

    private static String user = "user";

    private static String password = "password";

    public static Connection getConnection() throws Exception {
        Connection con;
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, user, password);

        return con;
    }
}
