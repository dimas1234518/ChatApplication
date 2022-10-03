package net.zhevakin.factory;

import net.zhevakin.models.Chat;
import net.zhevakin.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ChatFactoryImpl implements ChatFactory{

    private final ChatRepository chatRepository;
    // либо создаем чат, либо находим....


    @Autowired
    public ChatFactoryImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @PostConstruct
    public void init(){
        if (chatRepository.findChatById(Chat.PUBLIC.getId()) != null) return;
        Chat chat = new Chat();
        chat.setName(Chat.PUBLIC.getName());
        chatRepository.save(chat);
    }

    @Override
    public Chat createChat() {
        Chat chat = new Chat();
        chat.setName("New chat");
        return chatRepository.save(chat);
    }

    @Override
    public Chat getChat(Long id) {
        return chatRepository.findChatById(id);
    }
}
