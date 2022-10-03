package net.zhevakin.services;

import net.zhevakin.models.Chat;
import net.zhevakin.models.Message;
import net.zhevakin.models.User;
import net.zhevakin.repository.ChatRepository;
import net.zhevakin.repository.MessageRepository;
import net.zhevakin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class ChatService {

    ChatRepository chatRepository;

    UserRepository userRepository;

    MessageRepository messageRepository;

    private Message lastMessage;

    @Autowired
    public ChatService(ChatRepository chatRepository, UserRepository userRepository,
            MessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public synchronized Message sendMessage(Chat chat, Message message, User user) {

        if (chat.getMessages() == null) chat.setMessages(new HashSet<>());
        if (chat.getUsers() == null) chat.setUsers(new HashSet<>());
        User existUser = chat.getUsers().stream().filter(u -> user.getUsername().equals(u.getUsername()))
                                                .findFirst()
                                                .orElse(null);
        if (existUser == null) chat.getUsers().add(user);

        message.setUser(user);
        message.setChat(chat);
        lastMessage = messageRepository.save(message);
        chat.getMessages().add(message);
        chatRepository.save(chat);
        notifyAll();
        return lastMessage;

    }

    public synchronized Message waitForNewMassage(){
        safeWait();
        return lastMessage;
    }

    private void safeWait() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
