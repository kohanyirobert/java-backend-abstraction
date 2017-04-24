package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import service.MessageService;
import service.MessageServices;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/messages/*")
public final class MessageServlet extends HttpServlet {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private MessageService ms = MessageServices.getMemoryMessageService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OBJECT_MAPPER.writer().writeValue(resp.getOutputStream(), ms.getMessage(getMessageId(req)));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String text = req.getParameter("text");
        ms.updateMessage(getMessageId(req), title, text);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ms.deleteMessage(getMessageId(req));
    }

    private int getMessageId(HttpServletRequest req) {
        int index = req.getRequestURI().lastIndexOf('/');
        int id = Integer.valueOf(req.getRequestURI().substring(index + 1));
        return id;
    }
}
