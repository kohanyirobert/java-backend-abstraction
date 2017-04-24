package service;

import model.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MemoryMessageService implements MessageService {

    public static final MessageService INSTANCE = new MemoryMessageService();

    private static Integer NEXT_ID = 0;

    private final List<Message> messages = new ArrayList<>();

    MemoryMessageService() {
    }

    @Override
    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    @Override
    public Message getMessage(int id) {
        return messages.stream()
            .filter(m -> m.getId() == id)
            .findFirst()
            .orElseThrow(IllegalAccessError::new);
    }

    @Override
    public Message createMessage(String title, String text) {
        Message message = new Message(NEXT_ID, title, text);
        NEXT_ID++;
        messages.add(message);
        return message;
    }

    @Override
    public void updateMessage(int id, String title, String text) {
        Message oldMessage = getMessage(id);
        Message newMessage = new Message(id, title, text);
        messages.set(messages.indexOf(oldMessage), newMessage);
    }

    @Override
    public void deleteMessage(int id) {
        Message message = getMessage(id);
        messages.remove(messages.indexOf(message));
    }
}
