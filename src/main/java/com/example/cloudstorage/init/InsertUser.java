package com.example.cloudstorage.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.cloudstorage.entity.Role;
import com.example.cloudstorage.entity.UserEntity;
import com.example.cloudstorage.exceptions.Errors;
import com.example.cloudstorage.properties.StorageProperties;
import com.example.cloudstorage.repository.RoleRepository;
import com.example.cloudstorage.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class InsertUser implements ApplicationRunner {
    private static final String LOGIN = "user@mail.ru";
    private static final String PASSWORD = "user1234";
    private static final String  ROLE = "ROLE_USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Path rootLocation;

    private final PasswordEncoder bcryptEncoder;

    public InsertUser(UserRepository userRepository,
                      RoleRepository roleRepository, StorageProperties storageProperties, PasswordEncoder bcryptEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.rootLocation = Paths.get(storageProperties.getRootLocation());
        this.bcryptEncoder = bcryptEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        Role roleUser = new Role(1L, ROLE);
        roleRepository.save(roleUser);

        UserEntity user = new UserEntity();
        user.setLogin(LOGIN);
        user.setPassword(bcryptEncoder.encode(PASSWORD));
        user.setRole(roleUser);

        userRepository.save(user);

        // creating user directories
        userRepository.findAll().forEach(userPDO -> {
            try {
                Files.createDirectories(rootLocation.resolve(userPDO.getLogin()));
            } catch (IOException e) {
                throw new RuntimeException(Errors.COULD_NOT_CREATE_USER_DIRECTORIES.value() + e.getMessage());
            }
            System.out.println(userPDO);
        });
    }
}