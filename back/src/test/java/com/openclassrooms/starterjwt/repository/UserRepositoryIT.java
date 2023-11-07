package com.openclassrooms.starterjwt.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ExtendWith(MockitoExtension.class)
public class UserRepositoryIT {
    
    @InjectMocks
    private UserRepository userRepository;

    Long userId = 1L;
    User user;

    @BeforeEach
    public void init() {
        user = User.builder()
            .id(userId)
            .lastName("Basic")
            .firstName("User")
            .email("test@email.com")
            .password("password")
            .admin(false)
            .build();
    }

    @Test
    public void save_shouldReturn_user() {

        User savedUser = userRepository.save(user);
        
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void findAll_shouldReturn_allUsers() {
        
        Long userId2 = 2L;
        User user2 = User.builder()
            .id(userId2)
            .lastName("Second")
            .firstName("User")
            .email("user@email.com")
            .password("password")
            .admin(false)
            .build();

        userRepository.save(user);
        userRepository.save(user2);
        
        List<User> users = userRepository.findAll();
        
        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users.size()).isGreaterThan(0);
    }
    
    @Test
    public void findById_shouldReturn_user() {
        
        userRepository.save(user);
        
        User searchedUser = userRepository.findById(user.getId()).get();
        
        Assertions.assertThat(searchedUser).isNotNull();
    }

    @Test
    public void update_shouldReturn_user() {
        
        userRepository.save(user);
        
        User newUser = userRepository.findById(user.getId()).get();
        newUser.setFirstName("one");
        newUser.setLastName("last");

        User updatedUser = userRepository.save(newUser);
        
        Assertions.assertThat(updatedUser.getFirstName()).isNotNull();
        Assertions.assertThat(updatedUser.getLastName()).isNotNull();
    }

    @Test
    public void delete_shouldReturn_emptyUser() {
        
        userRepository.save(user);
        
        userRepository.deleteById(user.getId());
        Optional<User> returnedUser = userRepository.findById(user.getId());
        
        Assertions.assertThat(returnedUser).isEmpty();
    }    
}
