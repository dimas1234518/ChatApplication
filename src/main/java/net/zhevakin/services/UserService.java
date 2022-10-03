package net.zhevakin.services;

import net.zhevakin.models.User;
import net.zhevakin.models.UserData;
import net.zhevakin.models.enums.Provider;

public interface UserService {

    void processOAuthPostLogin(UserData userData);

    Provider getProvider();

    User getUser(String username);

}
