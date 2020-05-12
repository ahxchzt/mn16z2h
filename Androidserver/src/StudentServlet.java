import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {
    Account user=null;
    Account admin=null;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set the response content type
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        //          Get the module code from the request sent by the client
        String Module_code = request.getParameter("module_code").trim();
        try (PrintWriter out = response.getWriter()) {
            Map<String, List<Student>> params = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            //            Respond to requests
            List<Student> list=UserDAO.queryStudent(Module_code);
            //           Store the result in json format
            params.put("Result",list);
            jsonObject.put("params", params);
            out.write(jsonObject.toString());
            System.out.println(jsonObject.toString());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

}