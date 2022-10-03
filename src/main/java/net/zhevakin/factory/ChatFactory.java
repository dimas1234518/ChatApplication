package net.zhevakin.factory;

import net.zhevakin.models.Chat;

public interface ChatFactory {

    Chat createChat();

    Chat getChat(Long id);

}
