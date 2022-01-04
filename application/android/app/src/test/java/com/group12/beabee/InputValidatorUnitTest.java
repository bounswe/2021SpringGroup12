package com.group12.beabee;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InputValidatorUnitTest {
    @Test
    public void emailFormatValidatorTest() {
        String falseEmail = "merkemail";
        String falseEmail2 = "@merkemail";
        String falseEmail3 = "merk@";
        String falseEmail4 = "merk@email";
        String falseEmail5 = "merk@email.";
        String falseEmail6 = "merk@.email";
        String correctEmail = "merk@email.com";
        assertFalse(InputValidator.IsTextEmailFormat(falseEmail));
        assertFalse(InputValidator.IsTextEmailFormat(falseEmail2));
        assertFalse(InputValidator.IsTextEmailFormat(falseEmail3));
        assertFalse(InputValidator.IsTextEmailFormat(falseEmail4));
        assertFalse(InputValidator.IsTextEmailFormat(falseEmail5));
        assertFalse(InputValidator.IsTextEmailFormat(falseEmail6));
        assertTrue(InputValidator.IsTextEmailFormat(correctEmail));
    }

    @Test
    public void inputMinLenghtTest(){
        String onechars = "1";
        String twochars = "12";
        int two = 2;
        String nullText = null;
        String emptyText = "";
        assertFalse(InputValidator.IsTextMinimumLength(nullText, two));
        assertFalse(InputValidator.IsTextMinimumLength(emptyText, two));
        assertFalse(InputValidator.IsTextMinimumLength(onechars, two));
        assertTrue(InputValidator.IsTextMinimumLength(twochars, two));
    }

    @Test
    public void inputEmptyTextTest(){
        String twochars = "12";
        String nullText = null;
        String emptyText = "";
        assertFalse(InputValidator.IsTextNonEmpty(nullText));
        assertFalse(InputValidator.IsTextNonEmpty(emptyText));
        assertTrue(InputValidator.IsTextNonEmpty(twochars));
    }
}