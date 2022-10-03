package net.zhevakin.controllers;

import net.zhevakin.factory.ChatFactory;
import net.zhevakin.models.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;

@Controller
public class ChatController {


    private final ChatFactory chatFactory;

    @Autowired
    public ChatController(ChatFactory chatFactory) {
        this.chatFactory = chatFactory;
    }

    @GetMapping("/chat/{id}")
    public String getChat(@PathVariable String id) {

        Chat chat = chatFactory.getChat(Long.valueOf(id));
        if (chat == null) ResponseEntity.ok(new ArrayList<>());
        return "chat";

    }

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

}
