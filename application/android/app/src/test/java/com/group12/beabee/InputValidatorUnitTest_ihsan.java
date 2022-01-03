package com.group12.beabee;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InputValidatorUnitTest_ihsan {


    @Test
    public void inputLongerThanOneCharLengthTest(){

        String blank = " ";
        assertTrue(InputValidator_ihsan.IsQueryAtLeastOneCharacterLength(blank));
        String oneChar = "a";
        assertTrue(InputValidator_ihsan.IsQueryAtLeastOneCharacterLength(oneChar));
        String twoChars = "aa";
        assertTrue(InputValidator_ihsan.IsQueryAtLeastOneCharacterLength(twoChars));
        String nullText = null;
        assertFalse(InputValidator_ihsan.IsQueryAtLeastOneCharacterLength(nullText));
        String emptyText = "";
        assertFalse(InputValidator_ihsan.IsQueryAtLeastOneCharacterLength(emptyText));
    }

    @Test
    public void searchInputNotNullQueryTest(){

        String nullText = null;
        assertFalse(InputValidator_ihsan.IsSearchQueryNonNull(nullText));
        String emptyText = "";
        assertTrue(InputValidator_ihsan.IsSearchQueryNonNull(emptyText));
        String aNumber = "12";
        assertTrue(InputValidator_ihsan.IsSearchQueryNonNull(aNumber));
        String aText = "kkkk";
        assertTrue(InputValidator_ihsan.IsSearchQueryNonNull(aText));
        String space = " ";
        assertTrue(InputValidator_ihsan.IsSearchQueryNonNull(space));
    }
}