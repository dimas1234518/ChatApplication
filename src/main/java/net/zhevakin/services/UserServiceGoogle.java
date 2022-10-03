package net.zhevakin.services;

import net.zhevakin.models.UserData;
import net.zhevakin.repository.UserRepository;
import net.zhevakin.models.User;
import net.zhevakin.models.enums.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceGoogle implements UserService{

	private final UserRepository userRepository;

	@Autowired
	public UserServiceGoogle(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void processOAuthPostLogin(UserData user) {
		User existUser = userRepository.findUserByUsername(user.getUsername());
		
		if (existUser == null) {
			User newUser = new User();
			newUser.setUsername(user.getUsername());
			newUser.setEmail(user.getEmail());
			newUser.setProvider(getProvider());
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
		return Provider.GOOGLE;
	}
}
