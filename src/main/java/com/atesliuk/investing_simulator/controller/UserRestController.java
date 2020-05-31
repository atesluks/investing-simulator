package com.atesliuk.investing_simulator.controller;

import com.atesliuk.investing_simulator.domain.User;
import com.atesliuk.investing_simulator.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    public Iterable<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping(value = "/users/{userId}")
    public User getUser(@PathVariable Long userId){
        return userService.getUser(userId);
    }

    @PostMapping(value = "/users")
    public User addUser(@RequestBody User user){
        user.setId(0L);
        return userService.saveUser(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user){
        userService.saveUser(user);
        return user;
    }

    @DeleteMapping(value = "/users/{userId}")
    public String deleteUser(@PathVariable Long userId){
        User tempUser = userService.getUser(userId);
        if (userId == null){
            //throw new CustomerNotFoundException("Customer id not found - "+customerId);
            return "Error. User with id "+userId+" was not found.";
        }

        userService.deleteUser(tempUser);
        return "Deleted customer id - " + userId;
    }

}
