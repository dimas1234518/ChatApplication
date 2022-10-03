package net.zhevakin.services;

import net.zhevakin.models.User;
import net.zhevakin.models.UserData;
import net.zhevakin.models.enums.Provider;
import net.zhevakin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceLocal implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceLocal(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    //TODO: проверку на дубликаты
    @Override
    public void processOAuthPostLogin(UserData user) {

        User existUser = userRepository.findUserByUsername(user.getUsername());

        if (existUser == null) {
            User newUser = new User();
            newUser.setUsername(user.getUsername());
            newUser.setEmail(user.getEmail());
            newUser.setProvider(getProvider());
            newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            newUser.setEnabled(true);

            userRepository.save(newUser);

            System.out.println("Created new user: " + user.getUsername());
        }

    }

    @Override
    public User getUser(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public Provider getProvider() {
        return Provider.LOCAL;
    }
}
