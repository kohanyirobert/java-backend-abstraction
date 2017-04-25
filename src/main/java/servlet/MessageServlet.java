package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import service.MessageService;
import service.MessageServices;
import util.IO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> keyValuePairs = getKeyValuePairs(req);
        String title = keyValuePairs.get("title");
        String text = keyValuePairs.get("text");
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

    private Map<String, String> getKeyValuePairs(HttpServletRequest req) throws IOException {
        Map<String, String> result = new HashMap<>();
        String body = IO.toString(req.getInputStream());
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            int index = pair.indexOf('=');
            String key = pair.substring(0, index);
            String value = pair.substring(index + 1);
            result.put(key, value);
        }
        return result;
    }
}
