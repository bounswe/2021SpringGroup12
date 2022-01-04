package com.group12.beabee;

public class InputValidator {

    public static boolean IsTextNonEmpty(String input){
        if (input==null)
            return false;

        return input.length() > 0;
    }

    public static boolean IsTextMinimumLength(String input, int minimumLength){
        if (input==null)
            return false;

        return input.length() >= minimumLength;
    }

    public static boolean IsTextEmailFormat(String input){
        if (input==null)
            return false;

        boolean at = input.contains("@");
        if (!at)
            return false;

        int idxAt = input.indexOf('@');

        if (idxAt<1 || idxAt >= input.length()-1)
            return false;

        String first = input.substring(0,idxAt);
        String domain = input.substring(idxAt+1);

        if (!domain.contains("."))
            return false;

        if (domain.charAt(domain.length()-1) == '.')
            return false;

        if (domain.charAt(0) == '.')
            return false;

        return true;
    }
}
