package cmpe451.group12.beabee.common.util;

import java.math.BigInteger;
import java.util.UUID;

public class UUIDShortener
{
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String shortenUUID(UUID uuid) {
        BigInteger integer = new BigInteger(uuid.toString().replace("-",""), 16);
        StringBuilder sb = new StringBuilder("");
        while(integer.compareTo(BigInteger.ZERO)==1){
            BigInteger rem = integer.mod(new BigInteger("62"));
            sb.append(ALPHABET.charAt(rem.intValue()));
            integer = integer.divide(new BigInteger("62"));
        }
        return sb.reverse().toString();
    }

    public static String randomShortUUID() {
        return shortenUUID(UUID.randomUUID());
    }
}
