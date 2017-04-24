package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Message;
import service.MessageService;
import service.MessageServices;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/messages")
public final class MessagesServlet extends HttpServlet {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private MessageService ms = MessageServices.getMemoryMessageService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OBJECT_MAPPER.writer().writeValue(resp.getOutputStream(), ms.getMessages());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String text = req.getParameter("text");
        Message message = ms.createMessage(title, text);
        resp.setHeader("Location", String.valueOf(message.getId()));
    }
}
