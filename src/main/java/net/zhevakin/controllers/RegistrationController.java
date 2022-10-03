package net.zhevakin.controllers;

import net.zhevakin.models.UserData;
import net.zhevakin.models.enums.Provider;
import net.zhevakin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {

    private final Map<Provider, UserService> userServiceMap;

    private static final String REGISTER = "register";

    @Autowired
    public RegistrationController(Map<Provider, UserService> userServiceMap) {
        this.userServiceMap = userServiceMap;
    }

    @GetMapping("/register")
    public String register(final Model model){
        model.addAttribute("userData", new UserData());
        return REGISTER;
    }

    @PostMapping("/register")
    public String userRegistration(final @Valid  UserData userData, final BindingResult bindingResult, final Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("registrationForm", userData);
            return REGISTER;
        }
//        try {
            userServiceMap.get(Provider.LOCAL).processOAuthPostLogin(userData);
//        }catch (UserAlreadyExistException e){
//            bindingResult.rejectValue("email", "userData.email","An account already exists for this email.");
//            model.addAttribute("registrationForm", userData);
//            return REGISTER;
//        }
        return "index";
    }

}
