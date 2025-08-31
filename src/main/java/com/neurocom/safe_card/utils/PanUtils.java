package com.neurocom.safe_card.utils;

public class PanUtils {


    public static final int KEEP_START = 6;
    public static final int KEEP_END = 4;

    // add private constructor to prevent instantiation
    private PanUtils() {}


    /** Normalizes a PAN by removing all whitespace characters.
     * @param pan The input PAN (may contain spaces).
     * @return The normalized PAN (digits only).
     */
    public static String normalize(String pan) {
        return pan.replaceAll("\\s+", "");
    }

    /** Validates the length of a normalized PAN.
     * @param pan The normalized PAN (digits only).
     * @return True if the length is between 12 and 19 digits, false otherwise.
     */
    public static boolean isLengthValid(String pan) {
        int len = pan.length();
        return len >= 12 && len <= 19 && pan.matches("\\d+");
    }

    /** Validates a normalized PAN using the Luhn algorithm.
     * @param pan The normalized PAN (digits only).
     * @return True if the PAN is valid according to Luhn, false otherwise.
     */
    public static boolean isLuhnValid(String pan) {
        int sum = 0;
        boolean alt = false;
        for (int i = pan.length()-1; i >= 0; i--) {
            int n = pan.charAt(i) - '0';
            if (alt) { n *= 2; if (n > 9) n -= 9; }
            sum += n; alt = !alt;
        }
        return sum % 10 == 0;
    }

    /** Validates a PAN by normalizing it, checking length, and applying Luhn check.
     * @param pan The input PAN (may contain spaces).
     * @return True if the PAN is valid, false otherwise.
     */
    public static boolean isPanValid(String pan) {
        String normalized = normalize(pan);
        return isLengthValid(normalized) && isLuhnValid(normalized);
    }

    /** Masks a normalized PAN for display, showing only the first 6 and last 4 digits.
     * Groups digits in sets of 4 separated by spaces.
     * @param pan The normalized PAN (digits only).
     * @return The masked PAN (e.g. "123456******7890").
     */
    public static String maskPan(String pan) {

        String digits = normalize(pan);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digits.length(); i++) {
            boolean show = i < KEEP_START || i >= digits.length() - KEEP_END;
            sb.append(show ? digits.charAt(i) : '*');
            if ((i+1) % 4 == 0 && i != digits.length()-1) sb.append(' ');
        }
        return sb.toString();
    }
}