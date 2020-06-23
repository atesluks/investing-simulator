package com.atesliuk.investing_simulator.controller;

import com.atesliuk.investing_simulator.controller.exceptions.EntityNotFoundException;
import com.atesliuk.investing_simulator.domain.User;
import com.atesliuk.investing_simulator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * Controller class that handles REST API requests connected with User objects (User Entity)
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UserRestController {

    //Autowired User Service object that will be used further in the class for communication
    //with the database
    @Autowired
    private UserService userService;

    /**
     * The method handles GET request and returns the list of all users in the databse
     * @return - Iterable of User objects
     */
    @GetMapping("/users")
    public Iterable<User> getAllUsers(){
        return userService.getAllUsers();
    }

    /**
     * The method handles GET request and returns a particular user (the one with the provided id as a path variable)
     * @param userId - id of a searched user
     * @return - User object
     */
    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable Long userId){
        User theUser = userService.getUser(userId);

        //If the user with such id is not found, an exception is thrown
        if (theUser==null) throw new EntityNotFoundException("User id not found - "+userId);
        return theUser;
    }

    /**
     * The method handles POST request that saves a new user
     * @param theUser - a User object that has to be saved in the MySQL database
     * @return - the saved User object
     */
    @PostMapping("/users")
    public User addUser(@RequestBody User theUser){
        //set id of 0 in case there was some id provided (so the database assigns id itself)
        theUser.setId(0L);
        //creating an empty List of portfolios for that user
        theUser.setPortfolios(new ArrayList<>());

        //For debugging
        //System.out.println("UserRestController.addUser(): "+theUser);

        //capitalizing first letters in first name and last name in case they are lowercase
        theUser = capitaliseName(theUser);
        return userService.saveUser(theUser);
    }

    /**
     * The method handles PUT request that updates existing user
     * @param theUser - a User object in a form it has to be updated
     * @return - an updated User object
     */
    @PutMapping("/users")
    public User updateUser(@RequestBody User theUser){
        //capitalizing first letters in case they are lowercase
        theUser = capitaliseName(theUser);
        return userService.saveUser(theUser);
    }

    /**
     * The method handles DELETE request that deletes provided User object from the database
     * @param userId - id of a user that has to be deleted
     * @return - string with information if the user has been deleted or not
     */
    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable Long userId){
        //searching user with the provided id
        User theUser = userService.getUser(userId);

        //in case no user with such id was found, an according message is returned
        if (theUser == null){
            //throw new CustomerNotFoundException("Customer id not found - "+customerId);
            return "Error. User with id "+userId+" was not found.";
        }

        //if the user was foud, it is deleted and success message is returned
        userService.deleteUser(theUser);
        return "Deleted user id - " + userId;
    }

    /**
     * Helper method that handles POST request. The method checks if the provided password matches
     * an actual user's password
     * @param userId - an id of a user which has to be tested
     * @param testedPassword - the password that jas to be tested with the real password
     * @return - true if the provided password matches the real one, false otherwise
     */
    @PostMapping("/users/{userId}")
    public Boolean matchPassword(@PathVariable Long userId, @RequestBody String testedPassword){
        //searching a user with such id
        User theUser = userService.getUser(userId);

        //if no users found, an exception is thrown
        if (theUser==null) throw new EntityNotFoundException("User id not found - "+userId);

        //checking if provided password matches with the actual one
        boolean result = userService.matchPassword(theUser, testedPassword);

        //for logging and debugging
        System.out.println("Received request for password matching! The result: "+result);
        return result;
    }

    /**
     * A helper method tha handles a POST request that checks a user's login credentials
     * @param credentials - a String array in whcih first element is provided email, and second
     *                    element is provided password
     * @return - a User object that has such email and password in case credentials are correct,
     *           and null otherwise
     */
    @PostMapping("/login")
    public User login(@RequestBody String[] credentials){
        //Checking credentials through the USer Service
        User result = userService.login(credentials[0], credentials[1]);

        //In case no users were found with such credentials, an exception is thrown
        if (result==null){
            throw new NullPointerException("No users were found!");
        }else{
            //The actual User object is returned otherwise
            return result;
        }
    }

    //Helper method that capitalizes first and last name of a USer object
    // (and make all other letters lowercase)
    private User capitaliseName(User theUser){
        //capitalizing first letters in case they are lowercase
        String firstName = theUser.getFirstName();
        String lastName = theUser.getLastName();
        firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
        lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();
        theUser.setFirstName(firstName);
        theUser.setLastName(lastName);
        return theUser;
    }
}
