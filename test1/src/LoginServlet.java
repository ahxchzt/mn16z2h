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
@WebServlet("/LoginServlet")

public class LoginServlet extends HttpServlet {
    Account user=null;
    Account admin=null;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set the response content type
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        try (PrintWriter out = response.getWriter()) {

//          Get the account and password from the request sent by the client
            String accountNumber = request.getParameter("AccountNumber").trim();
            String password = request.getParameter("Password").trim();
            System.out.println(accountNumber);
            System.out.println(password);
//            Verify account and password
            Boolean verifyUser = verifyLogin(accountNumber, password);
            Boolean verifyifAdmin = verifyAdmin(accountNumber, password);
            Map<String, String> params = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
//           Store the result in json format
            if (verifyUser) {
                params.put("Result", "success");
                params.put("User", user.getId());
            } else if(verifyifAdmin){
                params.put("Result", "admin");
            }else{
                params.put("Result", "failed");
            }

            jsonObject.put("params", params);
            out.write(jsonObject.toString());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    private Boolean verifyLogin(String userEmail, String password) {
        user = UserDAO.queryUser(userEmail);
//            Verify account and password
        if (user != null && password.equals(user.getPassword())){
            return true;
        }else{
            return false;
        }

    }
    private Boolean verifyAdmin(String userEmail, String password) {
        admin = UserDAO.queryAdmin(userEmail);

//            Verify account and password
        if (admin != null && password.equals(admin.getPassword())){
            return true;
        }else{
            return false;
        }
    }
}

