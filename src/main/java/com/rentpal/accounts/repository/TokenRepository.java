package com.rentpal.accounts.repository;


import com.rentpal.accounts.model.Token;
import com.rentpal.accounts.model.TokenId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {

    @Modifying
    void deleteByTokenId(TokenId tokenId);

    Optional<Token> findById(@Param("id") String id);
}
