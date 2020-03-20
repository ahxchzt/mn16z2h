import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import net.sf.json.JSONObject;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 测试登录Servlet
 *
 * @author Implementist
 */
@WebServlet("/ModuleServlet")

public class ModuleServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set the response content type
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        //          Get the teacher id from the request sent by the client
        String teacher_id = request.getParameter("TeacherId").trim();
        try (PrintWriter out = response.getWriter()) {
            Map<String, List<Module>> params = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            //            Respond to requests
            List<Module> list=UserDAO.queryModule(teacher_id);
            //           Store the result in json format
            params.put("Result",list);
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

