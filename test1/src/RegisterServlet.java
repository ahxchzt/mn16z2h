import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import net.sf.json.JSONObject;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
@WebServlet("/RegisterServlet")

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set the response content type
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        try (PrintWriter out = response.getWriter()) {

            //          Get the user information from the request sent by the client
            String Name = request.getParameter("Name").trim();
            String Email = request.getParameter("Email").trim();
            String password = request.getParameter("Password").trim();
            String teacherId = request.getParameter("TeacherId").trim();

            Map<String, String> params = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            //  Judge if the Email or teacher id is existed. Store the result in json format
            if (verifyExist(Email,teacherId)){
                params.put("Result", "fail");
                jsonObject.put("params", params);
            }else{
                UserDAO.addUser(Email,teacherId,password,Name);
                params.put("Result", "success");
                jsonObject.put("params", params);

            }
            out.write(jsonObject.toString());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
    private Boolean verifyExist(String userEmail,String teacher_id) {
        Account user = UserDAO.queryUser(teacher_id);
        String email=UserDAO.existEmail(userEmail);
        return null != user || null!=email;

    }

}

