package org.centrifuge;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Encodes messages.
 */
public class MessageEncoder {

    private Logger logger = Logger.getLogger(getClass());

    public String hexEncodedHMacMD5Hash(String secret, String... messages) {
        return hexEncodedHMacHash(secret, "HMACMD5", messages);
    }

    public String hexEncodedHMacHash(String secret, String algorithm, String... messages) {
        byte[] result = hmacHash(secret, algorithm, messages);
        return new String(Hex.encodeHex(result));
    }

    private byte[] hmacHash(String secret, String algorithm, String... messages) {
        byte[] result = new byte[0];
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "ASCII");
            Mac mac = Mac.getInstance(algorithm);
            mac.init(keySpec);
            for (String message : messages) {
                mac.update(message.getBytes());
            }
            result = mac.doFinal();
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

}
