package com.neurocom.safe_card.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PanUtilsTest {

    public static final String PAN = "4539 1488 0343 6467";
    public static final String NORMALISED_PAN = "4539148803436467";
    @Test
    void testNormalize_removesSpaces() {
        assertEquals(NORMALISED_PAN, PanUtils.normalize(PAN));
    }

    @Test
    void testIsLengthValid_valid() {
        assertTrue(PanUtils.isLengthValid("411111111111")); // 12 digits
        assertTrue(PanUtils.isLengthValid("4111111111111111")); // 16 digits
        assertTrue(PanUtils.isLengthValid("4111111111111111111")); // 19 digits
    }

    @Test
    void testIsLengthValid_invalidLength() {
        assertFalse(PanUtils.isLengthValid("123")); // 3 digits, too short
        assertFalse(PanUtils.isLengthValid("12345678901234567890")); // 20 digits, too long
    }

    @Test
    void testIsLengthValid_nonDigits() {
        assertFalse(PanUtils.isLengthValid("4111abcd1111"));
    }

    @Test
    void testIsLuhnValid_valid() {
        assertTrue(PanUtils.isLuhnValid(NORMALISED_PAN)); // valid 16-digit PAN
    }

    @Test
    void testIsLuhnValid_invalid() {
        String pan = "4111111111111112"; // invalid PAN
        assertFalse(PanUtils.isLuhnValid(pan));
    }

    @Test
    void testMaskPan_standard() {
        String masked = PanUtils.maskPan(NORMALISED_PAN);
        // first 6 and last 4 visible, rest masked
        assertEquals("4539 14** **** 6467", masked);
    }

    @Test
    void testMaskPan_shortPan() {
        String pan = "1234567890";
        String masked = PanUtils.maskPan(pan);
        // first 6 and last 4 visible
        assertEquals("1234567890", masked.replaceAll(" ", ""));
    }

    @Test
    void testMaskPan_withSpaces() {
        String masked = PanUtils.maskPan(PAN);
        assertTrue(masked.startsWith("4539 14"));
        assertTrue(masked.endsWith("6467"));
        assertTrue(masked.contains("*"));
    }
}
