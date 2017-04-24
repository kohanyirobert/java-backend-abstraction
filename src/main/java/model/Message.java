package model;

import java.util.Objects;

public final class Message {

    private final int id;
    private final String title;
    private final String text;

    public Message(int id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id &&
            Objects.equals(title, message.title) &&
            Objects.equals(text, message.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, text);
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", text='" + text + '\'' +
            '}';
    }
}
