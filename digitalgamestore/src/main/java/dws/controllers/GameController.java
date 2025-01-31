package dws.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import dws.entities.Game;
import dws.repositories.GameRepository;

@RestController // Indicates this class handles HTTP requests and returns data directly (e.g., JSON).
@RequestMapping("/games") // Base URL path for all endpoints in this controller.
public class GameController {
    @Autowired
    private GameRepository gameRepository;

    /**
     * Get details of a specific game by its ID.
     * 
     * URL example: /games/1
     * 
     * @param gameId The ID of the game to fetch.
     * @return The Game object if found, or an empty Game object if not found.
     */
    @GetMapping("/{gameId}")
    public Game getGame(@PathVariable("gameId") int gameId) {
        try {
            Optional<Game> optionalGame = gameRepository.findById(gameId);
            return optionalGame.orElse(new Game());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving game", e);
        }
    }

    /**
     * Get a list of all games in the database.
     * 
     * URL example: /games/all
     * 
     * @return A list of Game objects representing all games in the database. If no games exist, an empty list is returned.
     */
    @GetMapping("/all")
    public List<Game> getAllGames() {
        try {
            return gameRepository.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving games", e);
        }
    }

    /**
     * Add a new game to the database by passing parameters through the URL.
     * 
     * URL example: /games/add?title=gameTitle&genre=Action&developer=developerName&releaseDate=2023-11-22&price=69.99&leasePrice=19.99&description=GameDescription
     * 
     * @param title The title of the new game.
     * @param genre The genre of the new game (must match the 64 SET options of the Game entity).
     * @param developer The developer of the new game.
     * @param releaseDate The release date of the new game in YYYY-MM-DD format.
     * @param price The purchase price of the new game.
     * @param leasePrice The lease price of the new game.
     * @param description A description of the new game.
     * 
     * @return The saved Game object or an empty Game object if the parameters are invalid.
     */
    @GetMapping("/add")
    public Game addGame(
            @RequestParam("title") String title,
            @RequestParam("genre") String genre,
            @RequestParam("developer") String developer,
            @RequestParam("releaseDate") String releaseDate,
            @RequestParam("price") double price,
            @RequestParam("leasePrice") double leasePrice,
            @RequestParam("description") String description) {
        try {
            Game game = new Game(title, genre, developer, releaseDate, price, leasePrice, description);
            return gameRepository.save(game);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid game parameters", e);
        }
    }

    /**
     * Delete a game from the database by its ID.
     * 
     * URL example: /games/delete/1
     * 
     * @param gameId The ID of the game to delete.
     * @return The deleted Game object if it exists, or an empty Game object if not found.
     */
    @GetMapping("/delete/{gameId}")
    public Game deleteGame(@PathVariable("gameId") int gameId) {
        try {
            Optional<Game> optionalGame = gameRepository.findById(gameId);
            if (optionalGame.isPresent()) {
                Game game = optionalGame.get();
                gameRepository.delete(game);
                return game;
            } else {
                return new Game();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting game", e);
        }
    }

    /**
     * Update the price of a specific game.
     * 
     * URL example: /games/1/price?newPrice=49.99
     * 
     * @param gameId   The ID of the game whose price is to be updated.
     * @param newPrice The new price value.
     * @return The updated Game object if it exists, or an empty Game object if not found.
     */
    @GetMapping("/{gameId}/price")
    public Game updateGamePrice(@PathVariable("gameId") int gameId, @RequestParam("newPrice") double newPrice) {
        try {
            Optional<Game> optionalGame = gameRepository.findById(gameId);
            if (optionalGame.isPresent()) {
                Game game = optionalGame.get();
                game.setPrice(newPrice);
                gameRepository.save(game);
                return game;
            } else {
                return new Game();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating game price", e);
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
