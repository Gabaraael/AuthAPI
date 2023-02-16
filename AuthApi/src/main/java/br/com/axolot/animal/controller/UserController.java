package br.com.axolot.animal.controller;

import br.com.axolot.animal.Service.UserService;
import br.com.axolot.animal.dtos.UserLogin;
import br.com.axolot.animal.dtos.UserPasswordChange;
import br.com.axolot.animal.dtos.UserRegister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity register(@RequestBody UserRegister userRegister) {
        userService.register(userRegister);
        return ResponseEntity.ok().body(true);
    }

    @PostMapping(path = "/login")
    public ResponseEntity login(@RequestBody UserLogin userLogin) {
        return ResponseEntity.ok().body(userService.login(userLogin));
    }

    //Deve ser um PUT
    @PutMapping(path = "/change-password")
    public ResponseEntity changePassword(@RequestBody UserPasswordChange userPasswordChange) {
        userService.changePassword(userPasswordChange);
        return ResponseEntity.ok().body(true);
    }
}
