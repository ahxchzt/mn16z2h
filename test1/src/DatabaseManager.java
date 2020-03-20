import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;


// This class is used for managing database, providing database connection
// and disconnection functions
public class DatabaseManager extends HttpServlet {

    ServletConfig config;
    private static String username;
    private static String password;
    private static String url;
    private static Connection connection;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.config = config;
        //Get database username
        username = config.getInitParameter("Username");
        //Get database password
        password = config.getInitParameter("Password");
        //Get database connection URL
        url = config.getInitParameter("Connection");
    }

//  Establish a connection with the database
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

//  Disconnect from the database
    public static void closeAll(Connection connection, Statement statement,
                                ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
