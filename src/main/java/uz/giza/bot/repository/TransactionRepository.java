package uz.giza.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.giza.bot.payme.Transaction;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionId(String transactionId);
}
