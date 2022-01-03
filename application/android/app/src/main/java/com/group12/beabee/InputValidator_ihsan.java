package com.group12.beabee;

public class InputValidator_ihsan {

    public static boolean IsSearchQueryNonNull(String input){
        if (input==null)
            return false;

        return input.length() >= 0;
    }

    public static boolean IsQueryAtLeastOneCharacterLength(String input){
        if (input==null)
            return false;

        return input.length() >= 1;
    }


}
