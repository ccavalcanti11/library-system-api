package com.librarysystem.controller;

import com.librarysystem.model.Loan;
import com.librarysystem.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@Tag(name = "Loans", description = "Loan management operations")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    @Operation(summary = "Get all loans", description = "Retrieve a list of all loans")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved loans")
    public ResponseEntity<List<Loan>> getAllLoans() {
        List<Loan> loans = loanService.findAll();
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get loan by ID", description = "Retrieve a specific loan by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Loan found"),
        @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    public ResponseEntity<Loan> getLoanById(
            @Parameter(description = "Loan ID", required = true) @PathVariable String id) {
        return loanService.findById(id)
                .map(loan -> ResponseEntity.ok(loan))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new loan", description = "Create a new book loan")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Loan created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid loan data or business rule violation")
    })
    public ResponseEntity<?> createLoan(
            @Parameter(description = "Book ID", required = true) @RequestParam String bookId,
            @Parameter(description = "Borrower ID", required = true) @RequestParam String borrowerId,
            @Parameter(description = "Due date (optional, defaults to 14 days from now)") @RequestParam(required = false) LocalDate dueDate) {
        try {
            Loan loan = loanService.createLoan(bookId, borrowerId, dueDate);
            return ResponseEntity.status(HttpStatus.CREATED).body(loan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/return")
    @Operation(summary = "Return a book", description = "Mark a loan as returned")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Book returned successfully"),
        @ApiResponse(responseCode = "404", description = "Loan not found"),
        @ApiResponse(responseCode = "400", description = "Loan cannot be returned")
    })
    public ResponseEntity<?> returnBook(
            @Parameter(description = "Loan ID", required = true) @PathVariable String id) {
        try {
            Loan loan = loanService.returnBook(id);
            return ResponseEntity.ok(loan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/renew")
    @Operation(summary = "Renew a loan", description = "Extend the due date of an active loan")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Loan renewed successfully"),
        @ApiResponse(responseCode = "404", description = "Loan not found"),
        @ApiResponse(responseCode = "400", description = "Loan cannot be renewed")
    })
    public ResponseEntity<?> renewLoan(
            @Parameter(description = "Loan ID", required = true) @PathVariable String id,
            @Parameter(description = "New due date", required = true) @RequestParam LocalDate newDueDate) {
        try {
            Loan loan = loanService.renewLoan(id, newDueDate);
            return ResponseEntity.ok(loan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a loan", description = "Remove a loan record from the system")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Loan deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    public ResponseEntity<Void> deleteLoan(
            @Parameter(description = "Loan ID", required = true) @PathVariable String id) {
        try {
            loanService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/borrower/{borrowerId}")
    @Operation(summary = "Get loans by borrower", description = "Retrieve all loans for a specific borrower")
    public ResponseEntity<List<Loan>> getLoansByBorrower(
            @Parameter(description = "Borrower ID", required = true) @PathVariable String borrowerId) {
        List<Loan> loans = loanService.findByBorrower(borrowerId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/borrower/{borrowerId}/active")
    @Operation(summary = "Get active loans by borrower", description = "Retrieve all active loans for a specific borrower")
    public ResponseEntity<List<Loan>> getActiveLoansByBorrower(
            @Parameter(description = "Borrower ID", required = true) @PathVariable String borrowerId) {
        List<Loan> loans = loanService.findActiveByBorrower(borrowerId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/book/{bookId}")
    @Operation(summary = "Get loans by book", description = "Retrieve all loans for a specific book")
    public ResponseEntity<List<Loan>> getLoansByBook(
            @Parameter(description = "Book ID", required = true) @PathVariable String bookId) {
        List<Loan> loans = loanService.findByBook(bookId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue loans", description = "Retrieve all overdue loans")
    public ResponseEntity<List<Loan>> getOverdueLoans() {
        List<Loan> loans = loanService.findOverdueLoans();
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/due-soon")
    @Operation(summary = "Get loans due soon", description = "Retrieve loans that are due within a specified number of days")
    public ResponseEntity<List<Loan>> getLoansDueSoon(
            @Parameter(description = "Number of days", required = true) @RequestParam int days) {
        List<Loan> loans = loanService.findLoansDueSoon(days);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/borrower/{borrowerId}/count")
    @Operation(summary = "Get active loan count", description = "Get the number of active loans for a borrower")
    public ResponseEntity<Long> getActiveLoanCount(
            @Parameter(description = "Borrower ID", required = true) @PathVariable String borrowerId) {
        long count = loanService.getActiveLoanCount(borrowerId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/borrower/{borrowerId}/can-borrow")
    @Operation(summary = "Check if borrower can borrow more", description = "Check if a borrower can borrow additional books")
    public ResponseEntity<Boolean> canBorrowMore(
            @Parameter(description = "Borrower ID", required = true) @PathVariable String borrowerId) {
        boolean canBorrow = loanService.canBorrowMore(borrowerId);
        return ResponseEntity.ok(canBorrow);
    }
}
