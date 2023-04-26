package br.com.axolot.animal.repository;

import br.com.axolot.animal.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Override
    ArrayList<UserEntity> findAll();

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String username);

}
