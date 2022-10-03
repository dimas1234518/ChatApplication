package net.zhevakin.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserData implements Serializable {

    private String username;

    private String email;

    private String password;

}
