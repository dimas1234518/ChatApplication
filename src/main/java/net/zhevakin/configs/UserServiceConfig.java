package net.zhevakin.configs;

import net.zhevakin.models.enums.Provider;
import net.zhevakin.services.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class UserServiceConfig {

    private final Map<Provider, UserService> myAutowireMap = new HashMap<>();

    @Bean
    @Qualifier("userServiceMap")
    public Map<Provider, UserService> userServiceMap(List<UserService> userServiceList) {
        for (UserService prettyText : userServiceList) {
            myAutowireMap.put(prettyText.getProvider(), prettyText);
        }
        return myAutowireMap;
    }

}
