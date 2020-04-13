import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/AbsentServlet")

public class AbsentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set the response content type
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        try (PrintWriter out = response.getWriter()) {
//          Get the student id and module code from the request sent by the client
            String studentId = request.getParameter("StudentId").trim();
            String moduleCode = request.getParameter("ModuleCode").trim();
//            Respond to requests
            UserDAO.queryAbsent(studentId,moduleCode);
            Map<String, String> params = new HashMap<>();
//            Store the result in json format
            JSONObject jsonObject = new JSONObject();
            params.put("Result", "success");
            jsonObject.put("params", params);
            out.write(jsonObject.toString());
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }


}
