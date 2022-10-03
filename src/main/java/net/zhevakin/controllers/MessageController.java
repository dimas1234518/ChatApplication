package net.zhevakin.controllers;

import net.zhevakin.factory.ChatFactory;
import net.zhevakin.models.Chat;
import net.zhevakin.models.Message;
import net.zhevakin.models.MessageDTO;
import net.zhevakin.models.User;
import net.zhevakin.repository.UserRepository;
import net.zhevakin.services.ChatService;
import net.zhevakin.services.MessageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages/")
public class MessageController {

    private final Integer messageCount;

    private final ChatFactory chatFactory;

    private final ChatService chatService;

    private final MessageService messageService;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public MessageController(UserRepository userRepository,
                             ChatFactory chatFactory,
                             ChatService chatService,
                             MessageService messageService,
                             @Value("${message.count}") Integer messageCount,
                             ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.chatFactory = chatFactory;
        this.chatService = chatService;
        this.messageService = messageService;
        this.messageCount = messageCount;
        this.modelMapper = modelMapper;
    }



    @GetMapping("notification")
    public ResponseEntity<MessageDTO> notifyAboutNewMessages() {
        MessageDTO message = convertToDto(chatService.waitForNewMassage());
        return ResponseEntity.ok(message);
    }


    @GetMapping("{id}")
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable Long id) {
        Chat chat = chatFactory.getChat(id);
        if (chat == null) ResponseEntity.ok(new ArrayList<>());
        List<MessageDTO> messages = messageService.getMessage(messageCount, chat).stream()
                .map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(messages);
    }

    @PostMapping
    public ResponseEntity<MessageDTO> postMessage(HttpServletRequest request, Principal principal, @RequestBody LinkedHashMap<String, Object> body) {

        Chat chat = chatFactory.getChat(Long.valueOf(request.getHeader("Chat")));
        if (chat == null) return ResponseEntity.notFound().build();

        User user = userRepository.findUserByUsername(principal.getName());
        Message message = new Message();
        message.setText((String) body.get("text"));
        message.setDateSend(new Date());

        chatService.sendMessage(chat, message, user);
        return ResponseEntity.ok(convertToDto(message));
    }

    private MessageDTO convertToDto(Message message) {
        MessageDTO dto =  modelMapper.map(message, MessageDTO.class);
        dto.setDateSend(message.getDateSend());
        return dto;
    }

}
