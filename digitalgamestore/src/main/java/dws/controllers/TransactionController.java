package dws.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import dws.entities.*;
import dws.repositories.*;

@RestController // Indicates this class handles HTTP requests and returns data directly (e.g., JSON).
@RequestMapping("/transactions") // Base URL path for all endpoints in this controller.
public class TransactionController {
    @Autowired // Autowired injects the TransactionRepository dependency
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameRepository gameRepository;

    /**
     * Retrieves details of a specific transaction by its ID.
     * 
     * URL example: /transactions/1
     * 
     * @param transactionId The ID of the transaction to fetch.
     * @return The Transaction object if found, or an empty Transaction object if not found.
     */
    @GetMapping("/{transactionId}")
    public Transaction getTransaction(@PathVariable("transactionId") int transactionId) {
        try {
            Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);
            return optionalTransaction.orElse(new Transaction());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving transaction", e);
        }
    }

    /**
     * Get a list of all transactions in the database.
     * 
     * URL example: /transactions/all
     * 
     * @return A list of Transaction objects representing all transactions in the database. If no transactions exist, an empty list is returned.
     */
    @GetMapping("/all")
    public List<Transaction> getAllTransactions() {
        try {
            return transactionRepository.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving transactions", e);
        }
    }

    /**
     * Creates a new transaction for a user by passing parameters through the URL.
     * 
     * URL example: /transactions/add?userId=1&gameId=2&transactionType=Purchase
     *
     * @param userId          The ID of the user for the transaction (must exist).
     * @param gameId          The ID of the game involved in the transaction (must exist).
     * @param transactionType The type of transaction (must be Purchase or Lease).
     * @return The saved Transaction object or an empty Transaction object if parameters are invalid or user/game not found.
     */
    @GetMapping("/add")
    public Transaction addTransaction(@RequestParam("userId") int userId, @RequestParam("gameId") int gameId,
                                        @RequestParam("transactionType") String transactionType) {
        try {
            // Find the user and game based on IDs
            Optional<User> optionalUser = userRepository.findById(userId);
            Optional<Game> optionalGame = gameRepository.findById(gameId);

            if (optionalUser.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            if (optionalGame.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
            }

            User user = optionalUser.get();
            Game game = optionalGame.get();

            // Get current date using LocalDate
            LocalDate currentDate = LocalDate.now();

            // Set expiryDate based on transactionType (if 'Lease' then adds 30 days to the currentDate, if 'Purchase' is null)
            String expiryDate = transactionType.equals("Lease") ? currentDate.plusDays(30).toString() : null;

            // Get amount from game price
            double amount = game.getPrice();

            Transaction transaction = new Transaction(user, game, transactionType, currentDate.toString(), expiryDate, amount);
            return transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transaction parameters", e);
        }
    }

    /**
     * Deletes a transaction from the database by its ID.
     * 
     * URL example: /transactions/delete/1
     * 
     * @param transactionId The ID of the transaction to delete.
     * @return The deleted Transaction object if it exists, or an empty Transaction object if not found.
     */
    @GetMapping("/delete/{transactionId}")
    public Transaction deleteTransaction(@PathVariable("transactionId") int transactionId) {
        try {
            Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);
            if (optionalTransaction.isPresent()) {
                Transaction transaction = optionalTransaction.get();
                transactionRepository.delete(transaction);
                return transaction;
            } else {
                return new Transaction();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting transaction", e);
        }
    }

    /**
     * Retrieves all transactions for a specific user by their ID.
     * 
     * URL example: /transactions/user/1
     * 
     * @param userId The ID of the user to get transactions for.
     * @return A list of Transaction objects for the user, or an empty list if user not found.
     */
    @GetMapping("/user/{userId}")
    public List<Transaction> getTransactionsByUserId(@PathVariable int userId) {
        try {
            return transactionRepository.findAllByUserId(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving transactions by user", e);
        }
    }

}
