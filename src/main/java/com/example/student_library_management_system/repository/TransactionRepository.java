package com.example.student_library_management_system.repository;

import com.example.student_library_management_system.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    /** Checks whether a book has any transaction history. */
    boolean existsByBookId(int bookId);
    /** Finds the latest transaction for a specific card and catalogue book. */
    /** Finds the newest unreturned loan for a card and catalogue book. */
    Optional<Transaction> findFirstByBook_IdAndCard_IdAndTransactionStatusInOrderByTransactionDateDescIdDesc(int bookId, int cardId, List<String> transactionStatuses);
    /** Finds a card's transactions ordered from newest to oldest. */
    List<Transaction> findByCardIdOrderByTransactionDateDesc(int cardId);
    /** Finds a book's transactions ordered from newest to oldest. */
    List<Transaction> findByBookIdOrderByTransactionDateDesc(int bookId);
    /** Finds all transactions ordered from newest to oldest. */
    List<Transaction> findAllByOrderByTransactionDateDesc();
}
