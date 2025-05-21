package com.track.expense;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ExpenseTrackerApp {
    private static List<Transaction> transactions = new ArrayList<>();
    private static final String DATA_FILE_NAME = "transactions_simple.csv"; 
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        loadTransactions(); 
        System.out.println("Welcome to Your  Expense Tracker!");

        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        addIncome();
                        break;
                    case 2:
                        addExpense();
                        break;
                    case 3:
                        viewMonthlySummary();
                        break;
                    case 4:
                        saveTransactions(); 
                        System.out.println("Transactions saved. Exiting. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); 
            }
            System.out.println("-------------------------");
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("Please choose an option:");
        System.out.println("1. Add Income");
        System.out.println("2. Add Expense");
        System.out.println("3. View Monthly Summary");
        System.out.println("4. Save & Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addIncome() {
        System.out.println("--- Add Income ---");
        try {
            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); 

            if (amount <= 0) {
                System.out.println("Invalid income");
                return;
            }

            System.out.println("Select category:");
            IncomeCategory[] incomeCategories = IncomeCategory.values();
            for (int i = 0; i < incomeCategories.length; i++) {
                System.out.println((i + 1) + ". " + incomeCategories[i].getDisplayName());
            }
            System.out.print("Enter category number: ");
            int categoryChoice = scanner.nextInt();
            scanner.nextLine(); 

            if (categoryChoice < 1 || categoryChoice > incomeCategories.length) {
                System.out.println("Invalid category choice.");
                return;
            }
            
            String categoryName = incomeCategories[categoryChoice - 1].name();

            System.out.print("Enter description (optional): ");
            String description = scanner.nextLine();

            LocalDate date = getDateInput("Enter date (YYYY-MM-DD, press Enter for today): ");

            Transaction income = new Transaction(TransactionType.INCOME, amount, categoryName, description, date);
            transactions.add(income);
            System.out.println("Income added successfully!");

        } catch (InputMismatchException e) {
            System.out.println("Invalid amount format. Please enter a number.");
            scanner.nextLine(); 
        }
    }

    private static void addExpense() {
        System.out.println("--- Add Expense ---");
        try {
            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); 

            if (amount <= 0) {
                System.out.println("Expense amount must be positive.");
                return;
            }

            System.out.println("Select category:");
            ExpenseCategory[] expenseCategories = ExpenseCategory.values();
            for (int i = 0; i < expenseCategories.length; i++) {
                System.out.println((i + 1) + ". " + expenseCategories[i].getDisplayName());
            }
            System.out.print("Enter category number: ");
            int categoryChoice = scanner.nextInt();
            scanner.nextLine(); 

            if (categoryChoice < 1 || categoryChoice > expenseCategories.length) {
                System.out.println("Invalid category choice.");
                return;
            }
          
            String categoryName = expenseCategories[categoryChoice - 1].name();

            System.out.print("Enter description (optional): ");
            String description = scanner.nextLine();

            LocalDate date = getDateInput("Enter date (YYYY-MM-DD, press Enter for today): ");

            Transaction expense = new Transaction(TransactionType.EXPENSE, amount, categoryName, description, date);
            transactions.add(expense);
            System.out.println("Expense added successfully!");

        } catch (InputMismatchException e) {
            System.out.println("Invalid amount format. Please enter a number.");
            scanner.nextLine(); 
        }
    }

    private static LocalDate getDateInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String dateStr = scanner.nextLine();
            if (dateStr.isEmpty()) {
                return LocalDate.now(); 
            }
            try {
                return LocalDate.parse(dateStr, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }

    private static void viewMonthlySummary() {
        System.out.println("--- View Monthly Summary ---");
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded yet.");
            return;
        }
        try {
            System.out.print("Enter year (e.g., " + LocalDate.now().getYear() + "): ");
            int year = scanner.nextInt();
            System.out.print("Enter month (1-12): ");
            int monthNumber = scanner.nextInt();
            scanner.nextLine(); 

            if (monthNumber < 1 || monthNumber > 12) {
                System.out.println("Invalid month. Please enter a number between 1 and 12.");
                return;
            }

            YearMonth yearMonth = YearMonth.of(year, monthNumber);
            System.out.println("Summary");

            
            List<Transaction> monthlyTransactions = new ArrayList<>();
            for (Transaction t : transactions) {
                if (t.getDate().getYear() == year && t.getDate().getMonthValue() == monthNumber) {
                    monthlyTransactions.add(t);
                }
            }
            

            if (monthlyTransactions.isEmpty()) {
                System.out.println("No transactions found for this month.");
                return;
            }

            double totalIncome = 0;
            double totalExpenses = 0;

            System.out.println("--- Transactions ---");
            for (Transaction t : monthlyTransactions) {
                System.out.println(t); 
                if (t.getType() == TransactionType.INCOME) {
                    totalIncome += t.getAmount();
                } else {
                    totalExpenses += t.getAmount();
                }
            }

            System.out.println("--- Totals ---");
            System.out.printf("Total Income: %.2f%n", totalIncome);
            System.out.printf("Total Expenses: %.2f%n", totalExpenses);
            System.out.printf("Net Balance: %.2f%n", (totalIncome - totalExpenses));

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numbers for year and month.");
            scanner.nextLine(); 
        }
    }

    private static void loadTransactions() {
        File file = new File(DATA_FILE_NAME);
        if (!file.exists()) {
            System.out.println("No existing transaction data file found (" + DATA_FILE_NAME + "). Starting fresh.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE_NAME))) {
            String line;
            
            
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty() || line.startsWith("#")) { 
                    continue;
                }
                Transaction transaction = Transaction.fromCsvString(line);
                if (transaction != null) {
                    transactions.add(transaction);
                } else {
                     System.out.println("Could not parse line " + lineNumber + " from file: " + line);
                }
            }
            System.out.println("Transactions loaded successfully from " + DATA_FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error loading transactions from file '" + DATA_FILE_NAME + "': " + e.getMessage());
        }
    }

    private static void saveTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions to save.");
            
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE_NAME))) {
            
            for (Transaction transaction : transactions) {
                writer.write(transaction.toCsvString());
                writer.newLine();
            }
            System.out.println("Transactions saved successfully to " + DATA_FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error saving transactions to file '" + DATA_FILE_NAME + "': " + e.getMessage());
        }
    }
}
