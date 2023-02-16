package br.com.axolot.animal.dtos;

import lombok.Data;
@Data
public class UserRegister {
    private String username;
    private String password;
    private String email;
    private Long age;
    private String nickname;
}
