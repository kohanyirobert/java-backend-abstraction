package service;

import model.Message;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.Assert.assertEquals;

public class MessageServiceTest {

    private Connection connection;
    private MemoryMessageService memoryMessageService;
    private DatabaseMessageService databaseMessageService;

    @Before
    public void before() throws Exception {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test");
        memoryMessageService = new MemoryMessageService();
        databaseMessageService = new DatabaseMessageService(connection);
        databaseMessageService.drop();
        databaseMessageService.init();
    }

    @After
    public void after() throws Exception {
        connection.close();
    }

    @Test
    public void testMemoryMessageService() {
        assertMessageService(memoryMessageService);
    }

    @Test
    public void testDatabaseMessageService() {
        assertMessageService(databaseMessageService);
    }

    private void assertMessageService(MessageService ms) {
        assertEquals(0, ms.getMessages().size());

        Message createdMessage = ms.createMessage("title", "text");

        assertEquals(1, ms.getMessages().size());

        Message fetchedMessage = ms.getMessage(createdMessage.getId());

        assertEquals(createdMessage, fetchedMessage);

        ms.updateMessage(createdMessage.getId(), "newTitle", "newText");

        fetchedMessage = ms.getMessage(createdMessage.getId());

        assertEquals(createdMessage.getId(), fetchedMessage.getId());
        assertEquals("newTitle", fetchedMessage.getTitle());
        assertEquals("newText", fetchedMessage.getText());

        Message messageA = ms.createMessage("a", "a");
        Message messageB = ms.createMessage("b", "b");
        Message messageC = ms.createMessage("c", "c");

        assertEquals(4, ms.getMessages().size());

        ms.deleteMessage(messageA.getId());
        ms.deleteMessage(messageB.getId());
        ms.deleteMessage(messageC.getId());

        assertEquals(1, ms.getMessages().size());
    }
}
