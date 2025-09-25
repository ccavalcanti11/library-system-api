package com.librarysystem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;

import java.time.LocalDate;

@Document(collection = "loans")
public class Loan {

    @Id
    private String id;

    @NotNull(message = "Book ID is required")
    @Indexed
    private String bookId;

    @NotNull(message = "Borrower ID is required")
    @Indexed
    private String borrowerId;

    @NotNull(message = "Loan date is required")
    private LocalDate loanDate;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;

    private LocalDate returnDate;

    private LoanStatus status;

    private Double fineAmount;

    private String notes;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    public enum LoanStatus {
        ACTIVE,
        RETURNED,
        OVERDUE,
        RENEWED
    }

    public Loan() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
        this.loanDate = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(14); // Default 2 weeks loan period
        this.status = LoanStatus.ACTIVE;
        this.fineAmount = 0.0;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public Double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(Double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void updateTimestamp() {
        this.updatedAt = LocalDate.now();
    }

    public boolean isOverdue() {
        return status == LoanStatus.ACTIVE && LocalDate.now().isAfter(dueDate);
    }

    public long getDaysOverdue() {
        if (isOverdue()) {
            return LocalDate.now().toEpochDay() - dueDate.toEpochDay();
        }
        return 0;
    }

    public void returnBook() {
        this.returnDate = LocalDate.now();
        this.status = LoanStatus.RETURNED;
        updateTimestamp();
    }
}
