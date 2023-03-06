package com.fdmgroup.projectmanagment.Service;

import com.fdmgroup.projectmanagment.Model.Region;
import com.fdmgroup.projectmanagment.Model.Role;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setup() throws Exception {
        userService = new UserService(userRepository);
    }

    @Test
    void getAllUsers_test() {
        User u1 = new User();
        User u2 = new User();
        List<User> users = new ArrayList<>();
        users.add(u1);
        users.add(u2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> users2 = userService.getAllUsers();

        verify(userRepository, times(1)).findAll();

        assertEquals(users, users2);
    }

    @Test
    void getUserById_test() {
        User user = new User();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User testUser = userService.findById(1L);

        verify(userRepository, times(1)).findById(1L);

        assertEquals(user, testUser);
    }

    @Test
    void getUserByIdException_test() {
        assertThrows(Exception.class, () -> userService.findById(3L));
    }

    @Test
    void saveUser_test() {
        User user = new User();
        userService.save(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteUser_test() {
        Long id = 1L;
        userService.delete(id);

        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void checkUserLogin() {
        User user = new User();
        String testUsername = "sarab";
        String testPassword = "23423423";

        when(userRepository.checkLogin(testUsername, testPassword)).thenReturn(user);

        User testUser = userService.checkLogin(testUsername, testPassword);

        verify(userRepository).checkLogin(testUsername, testPassword);
        assertEquals(user, testUser);
    }

    @Test
    void testCountUsers() {
        when(userRepository.countUsers()).thenReturn(Integer.valueOf(0));

        Integer result = userService.countUsers();
        Assertions.assertEquals(Integer.valueOf(0), result);
    }

    @Test
    void findByLastName_test() {
        when(userRepository.findByLastName(anyString())).thenReturn(List.of(new User("firstName", "lastName", "username", "email", "password", Region.AU, Role.Admin, true)));

        List<User> result = userService.findByLastName("lastName");
        Assertions.assertEquals(List.of(new User("firstName", "lastName", "username", "email", "password", Region.AU, Role.Admin, true)), result);
    }

    @Test
    void getAllTraineesExcept_test() {
        when(userRepository.findByRole(any())).thenReturn(List.of(new User("firstName", "lastName", "username", "email", "password", Region.AU, Role.Admin, true)));

        List<User> result = userService.getAllTraineesExcept(Long.valueOf(1));
        Assertions.assertEquals(List.of(new User("firstName", "lastName", "username", "email", "password", Region.AU, Role.Admin, true)), result);
    }

    @Test
    void etAllTrainees_test() {
        when(userRepository.findByRole(any())).thenReturn(List.of(new User("firstName", "lastName", "username", "email", "password", Region.AU, Role.Admin, true)));

        List<User> result = userService.getAllTrainees();
        Assertions.assertEquals(List.of(new User("firstName", "lastName", "username", "email", "password", Region.AU, Role.Admin, true)), result);
    }
}