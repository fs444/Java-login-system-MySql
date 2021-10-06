/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loginpackage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.sql.*;

/**
 *
 * @author david
 */
public class Operations {
    public static boolean isLogin(String username, String password, String usertype, JFrame frame) {
        
        String passwordMd5 = Operations.getPasswordMd5(password);
        
        try {
            Connection myConn = (Connection) MySQLConnection.getConnection();
            
            String mySqlQuery2 = "SELECT * FROM login WHERE username = ? AND password = ? AND usertype = ?";
            
            PreparedStatement ps = myConn.prepareStatement(mySqlQuery2);
            
            ps.setString(1, username);
            ps.setString(2, passwordMd5);
            ps.setString(3, usertype);
            
            ResultSet resultSet = ps.executeQuery();
            
            while (resultSet.next()) {
                LoginSession.UID = resultSet.getInt("uid");
                LoginSession.Usertype = resultSet.getString("usertype");
                LoginSession.Nickname = resultSet.getString("username");
                LoginSession.firstName = resultSet.getString("firstname");
                LoginSession.lastName = resultSet.getString("lastname");
                LoginSession.email = resultSet.getString("email");
                
                return true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
        }
        
        return false;
    }
    
    public static boolean saveUserInformation(String firstname, String lastname, String email, Integer uid, JFrame frame) {
        Integer updateInfoSuccess = 0;
        
        try {
            Connection myConn = (Connection) MySQLConnection.getConnection();
        
            PreparedStatement ps;
            ps = myConn.prepareStatement("UPDATE login SET firstname = ?, lastname = ?, email = ? WHERE uid = ?");
            ps.setString(1, firstname);
            ps.setString(2, lastname);
            ps.setString(3, email);
            ps.setInt(4, uid);

            updateInfoSuccess = ps.executeUpdate();

            ps.close();
            
//            JOptionPane.showMessageDialog(frame, "ok");
            return true;
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(frame, e);
            return false;
        }
        
    }
    
    public static Integer getUserIdByNameEmail(String firstname, String lastname, String email) {
        Integer uid = 0;
        
        try {
            Connection myConn = (Connection) MySQLConnection.getConnection();
            
            String mySqlQuery = "SELECT uid FROM login WHERE firstname = ? AND lastname = ? AND email = ?";
            
            PreparedStatement preparedStatement = myConn.prepareStatement(mySqlQuery);
            
            preparedStatement.setString(1, firstname);
            preparedStatement.setString(2, lastname);
            preparedStatement.setString(3, email);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                uid = resultSet.getInt("uid");
            }
            
            return uid;
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
            return -1;
        }
    }
    
    public static boolean passwordEquals(String newPassword, String confirmPassword) {
        if (newPassword.equals(confirmPassword)) {
            return true;
        } else {
            return false;
        }
    }
    
    public static Integer saveNewPassword(String password, Integer uid) {
        Integer successRestorePswd = 0;
        
        String passwordMd5 = Operations.getPasswordMd5(password);
        
        try {
            Connection myConn = (Connection) MySQLConnection.getConnection();

            PreparedStatement ps = myConn.prepareStatement("UPDATE login SET password = ? WHERE uid = ?");
            ps.setString(1, passwordMd5);
            ps.setInt(2, uid);

            successRestorePswd = ps.executeUpdate();

            ps.close();
            
            if (successRestorePswd > 0) {
                return 1;
            } else {
                return 0;
            }

//            JOptionPane.showMessageDialog(this, "ok");
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Some problem");
            return -1;
        }
    }
    
    public static Integer addNewUser(String username, String password, String usertype) {
        String passwordMd5 = Operations.getPasswordMd5(password);
        
        try {
            Connection myConn = (Connection) MySQLConnection.getConnection();
            
            PreparedStatement preparedStatement = myConn.prepareStatement("INSERT INTO login (uid, usertype, username, password, email, firstname, lastname) VALUES (null, ?, ?, ?, null, null, null)");
            
            preparedStatement.setString(1, usertype);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, passwordMd5);
            
            Integer row = preparedStatement.executeUpdate();
            
            if (row > 0) {
//                JOptionPane.showMessageDialog(this, "ok");
                
                return 1;
            } else {
//                JOptionPane.showMessageDialog(this, "Some problems");
                
                return 0;
            }
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, e);
            
            return -1;
        }
    }
    
    public static String getPasswordMd5(String password) {
        String passwordToHash = password;
        String generatedPassword = null;
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passwordToHash.getBytes());
            
            byte[] bytes = md.digest();
            
            StringBuilder sb = new StringBuilder();
            
            for(int i=0; i< bytes.length ;i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            
            generatedPassword = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) {
            System.out.println("Some problem");
        }
//        System.out.println(generatedPassword);

        return generatedPassword;
    }
}
