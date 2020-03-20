import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class UserDAO {

    public static Account queryUser(String Teacher_id) {
        //Get the connection object of the database
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //Generate SQL code
        StringBuilder sqlTeacher = new StringBuilder();
        sqlTeacher.append("SELECT * FROM teacher WHERE teacher_id=?");
        try {
            preparedStatement = connection.prepareStatement(sqlTeacher.toString());
            preparedStatement.setString(1, Teacher_id);
//          Store teacher information in an Account object and return the object to the client
            resultSet = preparedStatement.executeQuery();
            Account user = new Account();
            if (resultSet.next()) {
                user.setEmail(resultSet.getString("Email"));
                user.setPassword(resultSet.getString("Password"));
                user.setId(resultSet.getString("teacher_id"));
                return user;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DatabaseManager.closeAll(connection, preparedStatement, resultSet);
        }
    }
    public static String existEmail(String Teacher_id) {
        //Get the connection object of the database
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //Generate SQL code
        StringBuilder sqlTeacher = new StringBuilder();
        sqlTeacher.append("SELECT * FROM teacher WHERE email=?");
        try {
            preparedStatement = connection.prepareStatement(sqlTeacher.toString());
            preparedStatement.setString(1, Teacher_id);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("Email");
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DatabaseManager.closeAll(connection, preparedStatement, resultSet);
        }
    }
    public static Account queryAdmin(String email) {
        //Get the connection object of the database
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //Generate SQL code
        StringBuilder sqlAdmin = new StringBuilder();
        sqlAdmin.append("SELECT * FROM admin WHERE email=?");

//        Store admin information in an Account object and return the object to the client
        try {
            preparedStatement = connection.prepareStatement(sqlAdmin.toString());
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();
            Account admin = new Account();
            if (resultSet.next()) {
                admin.setEmail(resultSet.getString("Email"));
                admin.setPassword(resultSet.getString("Password"));
                return admin;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DatabaseManager.closeAll(connection, preparedStatement, resultSet);
        }
    }
    public static void addUser(String email,String teacherId,String password,String Name) {
        //Get the connection object of the database
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //Generate SQL code
        StringBuilder sqlTeacher = new StringBuilder();
        sqlTeacher.append("INSERT INTO teacher(teacher_id, email, password, name) VALUES ('"+teacherId+"', '"+email+"', '"+password+"', '"+Name+"')");
//        Execute SQL statement to add teacher information
        try {
            preparedStatement = connection.prepareStatement(sqlTeacher.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseManager.closeAll(connection, preparedStatement, resultSet);
        }
    }

}
