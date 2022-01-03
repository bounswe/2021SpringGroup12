package cmpe451.group12.beabee.common.util;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.UUID;

@Component
public class UUIDShortener
{
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String shortenUUID(UUID uuid) {
        BigInteger integer = new BigInteger(uuid.toString().replace("-",""), 16);
        StringBuilder sb = new StringBuilder("");
        while(integer.compareTo(BigInteger.ZERO)==1){
            BigInteger rem = integer.mod(new BigInteger("62"));
            sb.append(ALPHABET.charAt(rem.intValue()));
            integer = integer.divide(new BigInteger("62"));
        }
        return sb.reverse().toString();
    }

    public String randomShortUUID() {
        return shortenUUID(UUID.randomUUID());
    }
}
