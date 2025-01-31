package dws.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dws.entities.Transaction;

@Repository // No need to add this annotation since Spring Boot automatically detects repository interfaces if they extend JpaRepository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    /**
     * Custom query method to find all transactions for a specific user.
     * 
     * This method uses a JPQL query to retrieve all Transaction objects where the associated user's
     * userId matches the given parameter.
     * 
     * In this query, "Transaction t" refers to the Transaction entity, and "t.user.userId" accesses the userId
     * field of the associated User entity. The ":userId" parameter is method argument passed via @Param.
     * 
     * @param userId The ID of the user whose transactions are to be fetched.
     * @return A list of Transaction objects associated with the specified user.
     */
    @Query("SELECT t FROM Transaction t WHERE t.user.userId = :userId") // The @Query annotation allows writing custom JPQL (Java Persistence Query Language) queries.
    List<Transaction> findAllByUserId(@Param("userId") int userId);
    
}

/* What a Repository Does in Spring Boot:
 * A repository in Spring Data JPA acts as an abstraction over the database, allowing you to:
 * 
 * - Query, save, delete, or update rows in the database without writing raw SQL.
 * - Use built-in methods (save(), findById(), delete()) and extend them with custom queries (e.g., findByName()).
 * 
 * Each repository manages CRUD operations for one entity. 
 * If you have multiple entities, each one typically has its own table in the database, 
 * and a repository for those specific operations is necessary.
 */