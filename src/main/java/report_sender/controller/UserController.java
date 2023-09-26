package report_sender.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import report_sender.entity.User;
import report_sender.service.JsonParserService;
import report_sender.service.UserService;
import report_sender.service.exception.ServiceException;
import report_sender.service.impl.JsonParserServiceImpl;
import report_sender.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet("/user")
public class UserController extends HttpServlet {
    private UserService userService = new UserServiceImpl();
    private JsonParserService jsonParserService = new JsonParserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userFromBody = request.getReader()
                .lines().collect(Collectors.joining(System.lineSeparator()));
        User user = jsonParserService.getUserFromJson(userFromBody);

        try {
            user = userService.validateUser(user);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUser = objectMapper.writeValueAsString(user);
        PrintWriter writer = response.getWriter();
        writer.write(jsonUser);
    }
}
