package dws.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import dws.entities.User;
import dws.repositories.UserRepository;

@RestController // Indicates this class handles HTTP requests and returns data directly (e.g., JSON).
@RequestMapping("/users") // Base URL path for all endpoints in this controller.
public class UserController {
    @Autowired
    private UserRepository userRepository;

    /**
     * Get details of a specific user by their ID.
     * 
     * URL example: /users/1
     * 
     * @param userId The ID of the user to fetch.
     * @return The User object if found, or an empty User object if not found.
     */
    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") int userId) {
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            return optionalUser.orElse(new User());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving user", e);
        }
    }

    /**
     * Get a list of all users in the database.
     * 
     * URL example: /users/all
     * 
     * @return A list of User objects representing all users in the database. If no users exist, an empty list is returned.
     */
    @GetMapping("/all")
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving users", e);
        }
    }


    /**
     * Add a new user to the database by passing parameters through the URL.
     * 
     * URL example: /users/add?username=john&email=john@example.com&password=1234
     * 
     * @param username       The username of the new user.
     * @param email          The email address of the new user.
     * @param password       The password for the new user.
     * @return The saved User object or an error message if the parameters are invalid.
     */
    @GetMapping("/add")
    public User addUser(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password) {
        try {
            User user = new User(username, email, password);
            return userRepository.save(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user parameters", e);
        }
    }

    /**
     * Delete a user from the database by their ID.
     * 
     * URL example: /users/delete/1
     * 
     * @param userId The ID of the user to delete.
     * @return The deleted User object if it exists, or an empty User object if not found.
     */
    @GetMapping("/delete/{userId}")
    public User deleteUser(@PathVariable("userId") int userId) {
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                userRepository.delete(user);
                return user;
            } else {
                return new User();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting user", e);
        }
    }

    /**
     * Update the balance of a specific user.
     * 
     * URL example: /users/1/balance?newBalance=500.0
     * 
     * @param userId     The ID of the user whose balance is to be updated.
     * @param newBalance The new balance value.
     * @return The updated User object if it exists, or an empty User object if not found.
     */
    @GetMapping("/{userId}/balance")
    public User updateUserBalance(@PathVariable int userId, @RequestParam double newBalance) {
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setAccountBalance(newBalance);
                userRepository.save(user);
                return user;
            } else {
                return new User();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating user balance", e);
        }
    }

    /* Why use Optional<>?:
     * Optional<T> is a container introduced in Java 8 to represent a value that might or might not be present. 
     * 
     * - It forces developers to think about "no result" cases and handle them explicitly.
     * - It prevents runtime exceptions like NullPointerException.
     * - It makes your code more robust and easier to understand,
     * especially when working with database queries that might not always return a result.
     */

}
