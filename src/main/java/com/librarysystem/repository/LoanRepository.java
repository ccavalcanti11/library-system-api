package com.librarysystem.repository;

import com.librarysystem.model.Loan;
import com.librarysystem.model.Loan.LoanStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends MongoRepository<Loan, String> {

    List<Loan> findByBorrowerId(String borrowerId);

    List<Loan> findByBookId(String bookId);

    List<Loan> findByStatus(LoanStatus status);

    List<Loan> findByBorrowerIdAndStatus(String borrowerId, LoanStatus status);

    List<Loan> findByBookIdAndStatus(String bookId, LoanStatus status);

    @Query("{'dueDate': {$lt: ?0}, 'status': 'ACTIVE'}")
    List<Loan> findOverdueLoans(LocalDate currentDate);

    @Query("{'dueDate': {$gte: ?0, $lte: ?1}}")
    List<Loan> findLoansDueBetween(LocalDate startDate, LocalDate endDate);

    @Query("{'loanDate': {$gte: ?0, $lte: ?1}}")
    List<Loan> findLoansByDateRange(LocalDate startDate, LocalDate endDate);

    @Query("{'returnDate': {$gte: ?0, $lte: ?1}}")
    List<Loan> findReturnsInDateRange(LocalDate startDate, LocalDate endDate);

    long countByBorrowerIdAndStatus(String borrowerId, LoanStatus status);

    long countByBookIdAndStatus(String bookId, LoanStatus status);
}
