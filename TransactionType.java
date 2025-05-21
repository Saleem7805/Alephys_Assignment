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
import java.util.stream.Collectors;


enum TransactionType {
    INCOME,
    EXPENSE
}


enum IncomeCategory {
    SALARY("Salary"),
    BUSINESS("Business Income"),
    GIFTS("Gifts Received"),
    OTHER_INCOME("Other Income");

    private final String displayName;

    IncomeCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    
    public static IncomeCategory fromString(String text) {
        for (IncomeCategory b : IncomeCategory.values()) {
            if (b.name().equalsIgnoreCase(text) || b.displayName.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null; 
    }
}


enum ExpenseCategory {
    FOOD("Food "),
    RENT("Rent"),
    TRANSPORT("Transportation"),
    HEALTH("Health"),
    ENTERTAINMENT("Entertainment"),
    OTHER_EXPENSE("Other Expenses");

    private final String displayName;

    ExpenseCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    
    public static ExpenseCategory fromString(String text) {
        for (ExpenseCategory b : ExpenseCategory.values()) {
            if (b.name().equalsIgnoreCase(text) || b.displayName.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null; 
    }
}
