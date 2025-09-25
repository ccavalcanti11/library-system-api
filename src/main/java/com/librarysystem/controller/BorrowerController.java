package com.librarysystem.controller;

import com.librarysystem.model.Borrower;
import com.librarysystem.service.BorrowerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowers")
@Tag(name = "Borrowers", description = "Borrower management operations")
public class BorrowerController {

    private final BorrowerService borrowerService;

    @Autowired
    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @GetMapping
    @Operation(summary = "Get all borrowers", description = "Retrieve a list of all borrowers")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved borrowers")
    public ResponseEntity<List<Borrower>> getAllBorrowers() {
        List<Borrower> borrowers = borrowerService.findAll();
        return ResponseEntity.ok(borrowers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get borrower by ID", description = "Retrieve a specific borrower by their ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Borrower found"),
        @ApiResponse(responseCode = "404", description = "Borrower not found")
    })
    public ResponseEntity<Borrower> getBorrowerById(
            @Parameter(description = "Borrower ID", required = true) @PathVariable String id) {
        return borrowerService.findById(id)
                .map(borrower -> ResponseEntity.ok(borrower))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new borrower", description = "Register a new borrower in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Borrower created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid borrower data")
    })
    public ResponseEntity<Borrower> createBorrower(@Valid @RequestBody Borrower borrower) {
        try {
            Borrower savedBorrower = borrowerService.save(borrower);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBorrower);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a borrower", description = "Update an existing borrower")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Borrower updated successfully"),
        @ApiResponse(responseCode = "404", description = "Borrower not found"),
        @ApiResponse(responseCode = "400", description = "Invalid borrower data")
    })
    public ResponseEntity<Borrower> updateBorrower(
            @Parameter(description = "Borrower ID", required = true) @PathVariable String id,
            @Valid @RequestBody Borrower borrower) {
        try {
            Borrower updatedBorrower = borrowerService.update(id, borrower);
            return ResponseEntity.ok(updatedBorrower);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a borrower", description = "Remove a borrower from the system")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Borrower deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Borrower not found")
    })
    public ResponseEntity<Void> deleteBorrower(
            @Parameter(description = "Borrower ID", required = true) @PathVariable String id) {
        try {
            borrowerService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get borrower by email", description = "Retrieve a borrower by their email address")
    public ResponseEntity<Borrower> getBorrowerByEmail(
            @Parameter(description = "Borrower email", required = true) @PathVariable String email) {
        return borrowerService.findByEmail(email)
                .map(borrower -> ResponseEntity.ok(borrower))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    @Operation(summary = "Get active borrowers", description = "Retrieve all active borrowers")
    public ResponseEntity<List<Borrower>> getActiveBorrowers() {
        List<Borrower> borrowers = borrowerService.findActiveMembers();
        return ResponseEntity.ok(borrowers);
    }

    @GetMapping("/inactive")
    @Operation(summary = "Get inactive borrowers", description = "Retrieve all inactive borrowers")
    public ResponseEntity<List<Borrower>> getInactiveBorrowers() {
        List<Borrower> borrowers = borrowerService.findInactiveMembers();
        return ResponseEntity.ok(borrowers);
    }

    @GetMapping("/search")
    @Operation(summary = "Search borrowers", description = "Search borrowers by keyword in name, email, or city")
    public ResponseEntity<List<Borrower>> searchBorrowers(
            @Parameter(description = "Search keyword", required = true) @RequestParam String keyword) {
        List<Borrower> borrowers = borrowerService.searchBorrowers(keyword);
        return ResponseEntity.ok(borrowers);
    }

    @GetMapping("/city/{city}")
    @Operation(summary = "Get borrowers by city", description = "Retrieve all borrowers from a specific city")
    public ResponseEntity<List<Borrower>> getBorrowersByCity(
            @Parameter(description = "City name", required = true) @PathVariable String city) {
        List<Borrower> borrowers = borrowerService.findByCity(city);
        return ResponseEntity.ok(borrowers);
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate borrower", description = "Deactivate a borrower account")
    public ResponseEntity<Borrower> deactivateBorrower(
            @Parameter(description = "Borrower ID", required = true) @PathVariable String id) {
        try {
            Borrower deactivatedBorrower = borrowerService.deactivateMember(id);
            return ResponseEntity.ok(deactivatedBorrower);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/reactivate")
    @Operation(summary = "Reactivate borrower", description = "Reactivate a borrower account")
    public ResponseEntity<Borrower> reactivateBorrower(
            @Parameter(description = "Borrower ID", required = true) @PathVariable String id) {
        try {
            Borrower reactivatedBorrower = borrowerService.reactivateMember(id);
            return ResponseEntity.ok(reactivatedBorrower);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
