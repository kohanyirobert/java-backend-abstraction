package service;

import model.Message;
import util.IO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseMessageService implements MessageService {

    private final Connection connection;

    DatabaseMessageService(Connection connection) {
        this.connection = connection;
    }

    DatabaseMessageService() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void init() {
        executeScript("/init.sql");
    }

    void drop() {
        executeScript("/drop.sql");
    }

    @Override
    public List<Message> getMessages() {
        try {
            String sql = "SELECT `id`, `title`, `text` FROM `message`";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            List<Message> messages = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String text = rs.getString("text");
                messages.add(new Message(id, title, text));
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message getMessage(int id) {
        return getMessages().stream()
            .filter(m -> m.getId() == id)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public Message createMessage(String title, String text) {
        try {
            String sql = String.format("INSERT INTO `message`(`title`, `text`) VALUES ('%s', '%s')", title, text);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();
            while (rs.next()) {
                int id = rs.getInt(1);
                return new Message(id, title, text);
            }
            throw new IllegalArgumentException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateMessage(int id, String title, String text) {
        try {
            String sql = String.format("UPDATE `message` SET `title` = '%2$s', `text` = '%3$s' WHERE `id` = %1$s", id, title, text);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteMessage(int id) {
        try {
            String sql = String.format("DELETE FROM `message` WHERE id = %1$s", id);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeScript(String path) {
        try {
            String sql = IO.toString(System.class.getResourceAsStream(path));
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
