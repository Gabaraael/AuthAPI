package br.com.axolot.animal.Service;

import br.com.axolot.animal.dtos.UserLogin;
import br.com.axolot.animal.dtos.UserPasswordChange;
import br.com.axolot.animal.dtos.UserRegister;
import br.com.axolot.animal.model.UserEntity;
import br.com.axolot.animal.repository.UserRepository;
import br.com.axolot.animal.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;

@Service
public class UserService {

    private final UserRepository userRepository;

    private AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public void register(@NotNull UserRegister userRegister) {

        validateUserRegister(userRegister);

        String passwordEncoded = JwtUtils.encodePassword(userRegister.getPassword());
        userRegister.setPassword(passwordEncoded);

        userRepository.save(buildUser(userRegister));
    }

    public void validateUserRegister(UserRegister user) {
        if (checkUsernameExist(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This username is already in use");

        if (checkEmailExist(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This Email is already in use");
    }

    @Transactional
    public void changePassword(UserPasswordChange userPasswordChange) {
        UserEntity userEntity = findByUsername(userPasswordChange.getUsername());
        String rawOldPassword = userPasswordChange.getOldPassword();
        String encodedPassword = userEntity.getPassword();

        Boolean matchPassword = checkPassword(rawOldPassword, encodedPassword);

        if (matchPassword) {
            encodedPassword = JwtUtils.encodePassword(userPasswordChange.getNewPassword());
            userEntity.setPassword(encodedPassword);
            return;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password not match");
    }

    public String login(UserLogin userLogin) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword());

        Authentication authentication = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        var user = (UserEntity) authentication.getPrincipal();

        return  JwtUtils.generateToken(user);
    }

    public Boolean checkPassword(String rawPassword, String encodedPassword) {
        return JwtUtils.matchPassword(rawPassword, encodedPassword);
    }

    private Boolean checkUsernameExist(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    private Boolean checkEmailExist(String username) {
        return userRepository.findByEmail(username).isPresent();
    }


    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.CONFLICT, "This username does not exist"
                ));
    }

    public UserEntity buildUser(UserRegister userRegister) {
        return UserEntity.builder()
                .username(userRegister.getUsername())
                .password(userRegister.getPassword())
                .age(userRegister.getAge())
                .email(userRegister.getEmail())
                .nickname(userRegister.getPassword())
                .build();
    }
}
