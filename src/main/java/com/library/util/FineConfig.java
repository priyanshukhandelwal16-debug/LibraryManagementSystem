package com.library.util;

/**
 * Single place to configure library fine rules.
 */
public class FineConfig {
    // Fine charged per day for a late return (in rupees)
    public static final double FINE_PER_DAY = 5.0;

    // Default loan period in days when issuing a book
    public static final int DEFAULT_LOAN_DAYS = 7;

    private FineConfig() {}
}
