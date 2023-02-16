package br.com.axolot.animal.dtos;

import lombok.Data;

@Data
public class UserPasswordChange {
    private String username;
    private String newPassword;
    private String oldPassword;
}
