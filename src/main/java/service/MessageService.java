package service;

import model.Message;

import java.util.List;

public interface MessageService {

    List<Message> getMessages();

    Message getMessage(int id);

    Message createMessage(String title, String text);

    void updateMessage(int id, String title, String text);

    void deleteMessage(int id);

}
