package DataBase.Conn;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

public class ConnDBTest {
	//
    public static final boolean Connected() throws Throwable {
        boolean initialize = SQLiteJDBCLoader.initialize();

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:/home/users.sqlite");
        int i=0;
        try {
            ResultSet executeQuery = dataSource.getConnection()
                    .createStatement().executeQuery("select * from \"Table\"");
            while (executeQuery.next()) {
            	i++;
                System.out.println("out: "+executeQuery.getMetaData().getColumnLabel(i));

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        return initialize;
    }
}