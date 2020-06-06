package com.atesliuk.investing_simulator.controller;

import com.atesliuk.investing_simulator.controller.exceptions.EntityNotFoundException;
import com.atesliuk.investing_simulator.domain.User;
import com.atesliuk.investing_simulator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public Iterable<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable Long userId){
        User theUser = userService.getUser(userId);
        if (theUser==null) throw new EntityNotFoundException("User id not found - "+userId);
        return theUser;
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User theUser){
        theUser.setId(0L);

        //capitalizing first letters in case they are lowercase
        theUser = capitaliseName(theUser);

        return userService.saveUser(theUser);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User theUser){
        //capitalizing first letters in case they are lowercase
        theUser = capitaliseName(theUser);
        return userService.saveUser(theUser);
    }

    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable Long userId){
        User theUser = userService.getUser(userId);
        if (theUser == null){
            //throw new CustomerNotFoundException("Customer id not found - "+customerId);
            return "Error. User with id "+userId+" was not found.";
        }
        userService.deleteUser(theUser);
        return "Deleted user id - " + userId;
    }

    @PostMapping("/users/{userId}")
    public Boolean matchPassword(@PathVariable Long userId, @RequestBody String testedPassword){
        User theUser = userService.getUser(userId);
        if (theUser==null) throw new EntityNotFoundException("User id not found - "+userId);
        boolean result = userService.matchPassword(theUser, testedPassword);
        System.out.println("Received request for password amtching! The result: "+result);
        return result;
    }

    @PostMapping("/login")
    public User login(@RequestBody String[] credentials){
        User result = userService.login(credentials[0], credentials[1]);
        if (result==null){
            throw new NullPointerException("No users were found!");
        }else{
            return result;
        }
    }

    private User capitaliseName(User theUser){
        //capitalizing first letters in case they are lowercase
        String firstName = theUser.getFirstName();
        String lastName = theUser.getLastName();
        firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
        lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
        theUser.setFirstName(firstName);
        theUser.setLastName(lastName);
        return theUser;
    }


}
