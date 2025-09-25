package com.librarysystem.service;

import com.librarysystem.model.Loan;
import com.librarysystem.model.Loan.LoanStatus;
import com.librarysystem.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final BorrowerService borrowerService;

    private static final double DAILY_FINE_RATE = 0.50; // $0.50 per day
    private static final int MAX_LOANS_PER_BORROWER = 5;

    @Autowired
    public LoanService(LoanRepository loanRepository, BookService bookService, BorrowerService borrowerService) {
        this.loanRepository = loanRepository;
        this.bookService = bookService;
        this.borrowerService = borrowerService;
    }

    @Cacheable(value = "loans", key = "#id")
    public Optional<Loan> findById(String id) {
        return loanRepository.findById(id);
    }

    @Cacheable(value = "loans")
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    @CachePut(value = "loans", key = "#result.id")
    public Loan createLoan(String bookId, String borrowerId, LocalDate dueDate) {
        validateLoanCreation(bookId, borrowerId);

        Loan loan = new Loan();
        loan.setBookId(bookId);
        loan.setBorrowerId(borrowerId);
        if (dueDate != null) {
            loan.setDueDate(dueDate);
        }

        // Update book availability
        bookService.borrowBook(bookId);

        return loanRepository.save(loan);
    }

    @CachePut(value = "loans", key = "#id")
    public Loan returnBook(String id) {
        return loanRepository.findById(id)
            .map(loan -> {
                if (loan.getStatus() != LoanStatus.ACTIVE) {
                    throw new RuntimeException("Loan is not active and cannot be returned");
                }

                loan.returnBook();
                calculateFine(loan);

                // Update book availability
                bookService.returnBook(loan.getBookId());

                return loanRepository.save(loan);
            })
            .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));
    }

    @CachePut(value = "loans", key = "#id")
    public Loan renewLoan(String id, LocalDate newDueDate) {
        return loanRepository.findById(id)
            .map(loan -> {
                if (loan.getStatus() != LoanStatus.ACTIVE) {
                    throw new RuntimeException("Only active loans can be renewed");
                }
                if (loan.isOverdue()) {
                    throw new RuntimeException("Overdue loans cannot be renewed");
                }

                loan.setDueDate(newDueDate);
                loan.setStatus(LoanStatus.RENEWED);
                loan.updateTimestamp();

                return loanRepository.save(loan);
            })
            .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));
    }

    public List<Loan> findByBorrower(String borrowerId) {
        return loanRepository.findByBorrowerId(borrowerId);
    }

    public List<Loan> findActiveByBorrower(String borrowerId) {
        return loanRepository.findByBorrowerIdAndStatus(borrowerId, LoanStatus.ACTIVE);
    }

    public List<Loan> findByBook(String bookId) {
        return loanRepository.findByBookId(bookId);
    }

    public List<Loan> findOverdueLoans() {
        List<Loan> overdueLoans = loanRepository.findOverdueLoans(LocalDate.now());
        // Update status for overdue loans
        overdueLoans.forEach(loan -> {
            if (loan.getStatus() == LoanStatus.ACTIVE) {
                loan.setStatus(LoanStatus.OVERDUE);
                calculateFine(loan);
                loanRepository.save(loan);
            }
        });
        return overdueLoans;
    }

    public List<Loan> findLoansDueSoon(int days) {
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(days);
        return loanRepository.findLoansDueBetween(today, futureDate);
    }

    @CacheEvict(value = "loans", key = "#id")
    public void deleteById(String id) {
        if (!loanRepository.existsById(id)) {
            throw new RuntimeException("Loan not found with id: " + id);
        }
        loanRepository.deleteById(id);
    }

    public long getActiveLoanCount(String borrowerId) {
        return loanRepository.countByBorrowerIdAndStatus(borrowerId, LoanStatus.ACTIVE);
    }

    public boolean canBorrowMore(String borrowerId) {
        return getActiveLoanCount(borrowerId) < MAX_LOANS_PER_BORROWER;
    }

    private void validateLoanCreation(String bookId, String borrowerId) {
        // Check if book exists and is available
        if (!bookService.isBookAvailable(bookId)) {
            throw new RuntimeException("Book is not available for loan");
        }

        // Check if borrower exists and is active
        var borrower = borrowerService.findById(borrowerId)
            .orElseThrow(() -> new RuntimeException("Borrower not found"));

        if (!borrower.isActive()) {
            throw new RuntimeException("Borrower account is inactive");
        }

        // Check loan limits
        if (!canBorrowMore(borrowerId)) {
            throw new RuntimeException("Borrower has reached maximum loan limit");
        }

        // Check for existing active loan of the same book
        List<Loan> existingLoans = loanRepository.findByBorrowerIdAndStatus(borrowerId, LoanStatus.ACTIVE);
        boolean hasBookAlready = existingLoans.stream()
            .anyMatch(loan -> loan.getBookId().equals(bookId));

        if (hasBookAlready) {
            throw new RuntimeException("Borrower already has an active loan for this book");
        }
    }

    private void calculateFine(Loan loan) {
        if (loan.isOverdue()) {
            long daysOverdue = loan.getDaysOverdue();
            double fineAmount = daysOverdue * DAILY_FINE_RATE;
            loan.setFineAmount(fineAmount);
        }
    }
}
