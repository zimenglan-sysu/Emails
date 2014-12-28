package DataBase.Conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;   

public class ConnectDataBase {

    public static Connection con = null;

    public static Connection getDBConnection(){

        try{
            //String dir = System.getProperty("user.dir");
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:D:\\testdb.db");
            return con;
        }
        catch(ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null,"Problem with connection of database");
            return null;
        }
    }

}