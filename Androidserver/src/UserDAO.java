import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    public static List<Module> queryModule(String teacher_id) {
        //Get the connection object of the database
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //Generate SQL code
        StringBuilder sqlModule = new StringBuilder();
        sqlModule.append("SELECT * FROM module where teacher_id='"+teacher_id+"'");
//        Store module information in a list and return the list to the client
        try {
            preparedStatement = connection.prepareStatement(sqlModule.toString());
            resultSet = preparedStatement.executeQuery();
            List<Module> list=new ArrayList<Module>();
            while(resultSet.next()){
                Module module=new Module();
                module.setModule_code(resultSet.getString(1));
                module.setModule_name(resultSet.getString(2));
                module.setTime(resultSet.getString(3));
                module.setData(resultSet.getString(4));
                list.add(module);
            }
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DatabaseManager.closeAll(connection, preparedStatement, resultSet);
        }
    }

    public static List<Student> queryStudent(String module_code){
//        Get the connection object of the database
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //Generate SQL code
        StringBuilder sqlModule = new StringBuilder();
        sqlModule.append("select * from student,student_module where student_module.module_code='"+module_code+"' and student_module.student_id=student.student_id;");
//        Store student information in a List and return the list to the client
        try {
            preparedStatement = connection.prepareStatement(sqlModule.toString());
            resultSet = preparedStatement.executeQuery();
            List<Student> list=new ArrayList<Student>();
            while(resultSet.next()){
                Student student=new Student();
                student.setAbsent_time(resultSet.getInt(1));
                student.setName(resultSet.getString(2));
                student.setAddress(resultSet.getString(6));
                student.setEmail(resultSet.getString(4));
                student.setStudent_id(resultSet.getString(3));
                list.add(student);
            }
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DatabaseManager.closeAll(connection, preparedStatement, resultSet);
        }
    }
    public static void queryAbsent(String student_id,String module_code){
//        Get the connection object of the database
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //Generate SQL code
        StringBuilder sqlModule = new StringBuilder();
        StringBuilder sqlModule2 = new StringBuilder();
        sqlModule.append("update student set absent_time = absent_time+1 where student_id='"+student_id+"';");
        sqlModule2.append("update student_module set absent_time = absent_time+1 where student_id='"+student_id+"' and module_code='"+module_code+"';");
//        Execute the SQL statement to increase the number of absenteeism of the corresponding student by 1
        try {
            preparedStatement = connection.prepareStatement(sqlModule.toString());
            preparedStatement.executeUpdate();
            preparedStatement=connection.prepareStatement(sqlModule2.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseManager.closeAll(connection, preparedStatement, resultSet);
        }

    }
}
