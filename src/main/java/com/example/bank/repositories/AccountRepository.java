package com.example.bank.repositories;

import com.example.bank.dto.DepositRequest;
import com.example.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    @Query(value = "select * from Account a where a.account_number =:accountNumber",nativeQuery = true)
    Optional<Account> findByAccountNumber(@Param("accountNumber")
                                                  int accountNumber);
}
