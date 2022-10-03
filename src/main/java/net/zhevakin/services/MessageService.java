package net.zhevakin.services;

import net.zhevakin.models.Chat;
import net.zhevakin.models.Message;
import net.zhevakin.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {


    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getMessage(Integer count, Chat chat) {

         return messageRepository.findTopByChatsIs(PageRequest.of(0,count), chat);

    }



}
