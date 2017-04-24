package service;

public final class MessageServices {

    private MessageServices() {
    }

    public static MessageService getMemoryMessageService() {
        return MemoryMessageService.INSTANCE;
    }

    public static MessageService getDatabaseMessageService() {
        return new DatabaseMessageService();
    }
}
