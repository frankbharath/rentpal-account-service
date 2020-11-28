package com.rentpal.accounts.repository;

import com.rentpal.accounts.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE #{#entityName} u set u.password=:password where u.id=:userId")
    void updatePassword(@Param("userId") Long userId, @Param("password") String password);

    @Modifying
    @Query("UPDATE #{#entityName} u set u.verified=true where u.id=:userId")
    void verifyUser(@Param("userId") Long userId);

    Optional<User> findByEmail(String email);

}
