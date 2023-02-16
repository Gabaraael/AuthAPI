package br.com.axolot.animal.Service;

import br.com.axolot.animal.dtos.UserLogin;
import br.com.axolot.animal.dtos.UserPasswordChange;
import br.com.axolot.animal.dtos.UserRegister;
import br.com.axolot.animal.model.UserEntity;
import br.com.axolot.animal.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.Optional;  

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(@NotNull UserRegister userRegister) {

        if (checkUsernameExist(userRegister.getUsername()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This username is already in use");

        String passwordEncoded = passwordEncoder.encode(userRegister.getPassword());
        userRegister.setPassword(passwordEncoded);
        userRepository.save(buildUser(userRegister));
    }

    @Transactional
    public void changePassword(UserPasswordChange userPasswordChange) {
        UserEntity userEntity = findByUsername(userPasswordChange.getUsername());
        String rawOldPassword = userPasswordChange.getOldPassword();
        String encodedPassword = userEntity.getPassword();

        Boolean matchPassword = checkPassword(rawOldPassword, encodedPassword);

        if (matchPassword) {
            encodedPassword = encodePassword(userPasswordChange.getNewPassword());
            userEntity.setPassword(encodedPassword);
            return;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password not match");
    }

    public Boolean login(UserLogin userLogin) {
        UserEntity user = findByUsername(userLogin.getUsername());
        String rawPassword = userLogin.getPassword();
        String encodedPassword = user.getPassword();

        return checkPassword(rawPassword, encodedPassword);
    }

    public Boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public Boolean checkUsernameExist(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public UserEntity findByUsername(String username) {
        Optional<UserEntity> user = userRepository.findByUsername(username);

        if (user.isPresent())
            return user.get();

        throw new ResponseStatusException(
                HttpStatus.CONFLICT, "This username not exist"
        );
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
