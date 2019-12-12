package ee.gr;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class AesCrypt {

    private Cipher cipherEncrypt = Cipher.getInstance("AES");
    private Cipher cipherDecrypt = Cipher.getInstance("AES");
    private KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    private SecretKey key;

    public AesCrypt() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        this.keyGenerator.init(128);
        this.key = keyGenerator.generateKey();
        this.cipherEncrypt.init(Cipher.ENCRYPT_MODE, key);
        this.cipherDecrypt.init(Cipher.DECRYPT_MODE, key);
    }

    public String encrypt(String string) throws BadPaddingException, IllegalBlockSizeException {
        byte[] encryptedBytes = cipherEncrypt.doFinal(string.getBytes());
        return java.util.Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String string) throws BadPaddingException, IllegalBlockSizeException {
        byte[] decryptedBytes = java.util.Base64.getDecoder().decode(string.getBytes());
        return new String(cipherDecrypt.doFinal(decryptedBytes));
    }

}
