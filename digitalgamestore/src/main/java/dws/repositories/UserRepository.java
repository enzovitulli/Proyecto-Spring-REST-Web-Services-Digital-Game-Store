package dws.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dws.entities.User;

@Repository // No need to add this annotation since Spring Boot automatically detects repository interfaces if they extend JpaRepository
public interface UserRepository extends JpaRepository<User, Integer> {
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