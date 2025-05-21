package com.track.expense;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class Transaction {
    private TransactionType type;
    private double amount;
    private String category; 
    private String description;
    private LocalDate date;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Transaction(TransactionType type, double amount, String category, String description, LocalDate date) {
        if (amount <= 0) {
            
        }
        this.type = type;
        this.amount = amount;
        this.category = category; 
        this.description = (description == null || description.trim().isEmpty()) ? "N/A" : description;
        this.date = date;
    }

    
    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        String categoryDisplayName = category; 
        try {
            if (type == TransactionType.INCOME && IncomeCategory.fromString(category) != null) {
                categoryDisplayName = IncomeCategory.valueOf(category).getDisplayName();
            } else if (type == TransactionType.EXPENSE && ExpenseCategory.fromString(category) != null) {
                categoryDisplayName = ExpenseCategory.valueOf(category).getDisplayName();
            }
        } catch (IllegalArgumentException e) {
           
             System.err.println("Warning: Unknown category '" + category + "' for type " + type + ". Displaying raw category name.");
        }

        return String.format("[%s] Date: %s, Amount: %.2f, Category: %s, Description: %s",
                type, date.format(DATE_FORMATTER), amount, categoryDisplayName, description);
    }

    
    public String toCsvString() {
     
        return String.join(",",
                type.name(),
                String.valueOf(amount),
                category, 
                description.replace(",", ";"), 
                date.format(DATE_FORMATTER)
        );
    }

    
    public static Transaction fromCsvString(String csvLine) {
        if (csvLine == null || csvLine.trim().isEmpty()) {
            return null;
        }
        String[] parts = csvLine.split(",", 5); 
        if (parts.length < 5) {
            System.err.println("Skipping malformed CSV line (not enough parts): " + csvLine);
            return null;
        }

        try {
            TransactionType type = TransactionType.valueOf(parts[0].trim().toUpperCase());
            double amount = Double.parseDouble(parts[1].trim());
            String categoryName = parts[2].trim().toUpperCase(); 
            String description = parts[3].trim().replace(";", ","); 
            LocalDate date = LocalDate.parse(parts[4].trim(), DATE_FORMATTER);

            
            if (type == TransactionType.INCOME && IncomeCategory.fromString(categoryName) == null) {
                 System.err.println("Warning: CSV contains unknown income category '" + categoryName + "'. Storing as is.");
            } else if (type == TransactionType.EXPENSE && ExpenseCategory.fromString(categoryName) == null) {
                 System.err.println("Warning: CSV contains unknown expense category '" + categoryName + "'. Storing as is.");
            }

            return new Transaction(type, amount, categoryName, description, date);
        } catch (NumberFormatException e) {
            System.err.println("Skipping malformed CSV line (invalid amount): " + csvLine);
            return null;
        } catch (DateTimeParseException e) {
            System.err.println("Skipping malformed CSV line (invalid date): " + csvLine);
            return null;
        } catch (IllegalArgumentException e) { 
            System.err.println("Skipping malformed CSV line (invalid transaction type): " + csvLine);
            return null;
        }
    }
}
