package DataBase.Conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;  

import Util.Conn.*;
import Util.Email.PathManager;

/**
 * @purpose 维持sqlite数据库连接
 * @author ddk
 * @time 2014-12-07
 */
public class ConnDB {
	// instance a sqlite3 db
    public static Connection conn = null;
    public static String prefix = "jdbc:sqlite:";
    public static String pathToDB = null;
    
    // get conn
    public static Connection getConn(){
        try{
        	if(conn == null) {
	            //String dir = System.getProperty("user.dir");
	            Class.forName("org.sqlite.JDBC");
	            pathToDB = prefix + PathManager.getDBPath();
	            Connection con = DriverManager.getConnection(pathToDB);
        	}
            return conn;
        }
        catch(ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null, 
            		"Problem with connection of database");
            return null;
        }
    }
}